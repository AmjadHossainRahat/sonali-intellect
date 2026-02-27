package com.example.atm.socket;

import com.example.atm.config.AtmListenerProperties;
import com.example.atm.config.AtmListenerPropertiesLoader;
import com.example.atm.iso.Iso8583DummyProcessor;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Timer;
import io.micrometer.prometheusmetrics.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Non-Boot multi-port TCP listener for load testing.
 *
 * Framing: reads until socket read timeout occurs (or connection closes).
 * Works well with JMeter TCP Sampler in "close connection" mode or with a short read timeout.
 */
@Component
public class MultiPortSocketListener implements SmartLifecycle {

    private static final Logger log = LoggerFactory.getLogger(MultiPortSocketListener.class);

    private final Iso8583DummyProcessor processor = new Iso8583DummyProcessor();
    private final AtomicBoolean running = new AtomicBoolean(false);

    private final AtomicInteger activeConnections = new AtomicInteger(0);

    private final PrometheusMeterRegistry registry;
    private final Counter requestsTotal;
    private final Counter requestsErrors;
    private final Counter slaViolationsTotal;
    private final Timer requestDuration;

    
public MultiPortSocketListener(PrometheusMeterRegistry registry) {
    this.registry = registry;

    this.requestsTotal = Counter.builder("atm_socket_requests_total")
            .description("Total number of socket requests received")
            .register(registry);

    this.requestsErrors = Counter.builder("atm_socket_requests_errors_total")
            .description("Total number of socket requests that resulted in an internal error")
            .register(registry);

    this.slaViolationsTotal = Counter.builder("atm_socket_sla_violations_total")
            .description("Total number of socket requests that exceeded SLA")
            .register(registry);

    this.requestDuration = Timer.builder("atm_socket_request_duration_seconds")
            .description("Socket request end-to-end processing duration")
            .publishPercentiles(0.5, 0.95, 0.99)
            .register(registry);

    Gauge.builder("atm_socket_active_connections", activeConnections, AtomicInteger::get)
            .description("Active TCP connections being handled right now")
            .register(registry);
}

private AtmListenerProperties props;
    private final List<ServerSocket> servers = new ArrayList<>();
    private ExecutorService acceptorPool;
    private ExecutorService workerPool;
    private Semaphore connectionSemaphore;

    @Override
    public void start() {
        if (running.get()) return;

        this.props = AtmListenerPropertiesLoader.load();

        this.acceptorPool = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r);
            t.setName("acceptor-" + t.getId());
            t.setDaemon(true);
            return t;
        });

        this.workerPool = new ThreadPoolExecutor(
                props.getWorkerThreads(),
                props.getWorkerThreads(),
                30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                r -> {
                    Thread t = new Thread(r);
                    t.setName("worker-" + t.getId());
                    t.setDaemon(true);
                    return t;
                }
        );

        this.connectionSemaphore = new Semaphore(props.getMaxConnections());
        running.set(true);

        if (props.getPorts() == null || props.getPorts().isEmpty()) {
            log.warn("No ports configured in atm-listeners.properties. TCP listeners will NOT start.");
            return;
        }

        for (Integer port : props.getPorts()) {
            try {
                ServerSocket ss = new ServerSocket();
                ss.setReuseAddress(true);
                ss.bind(new InetSocketAddress(port));
                ss.setSoTimeout(props.getAcceptTimeoutMs());
                servers.add(ss);

                acceptorPool.submit(() -> acceptLoop(ss, port));
                log.info("TCP listener started on port {}", port);
            } catch (IOException e) {
                log.error("Failed to start listener on port {}: {}", port, e.toString());
            }
        }
    }

    @Override
    public void stop() {
        running.set(false);
        for (ServerSocket ss : servers) {
            try { ss.close(); } catch (Exception ignored) {}
        }
        servers.clear();
        if (acceptorPool != null) acceptorPool.shutdownNow();
        if (workerPool != null) workerPool.shutdownNow();
        log.info("TCP listeners stopped.");
    }

    @Override
    public boolean isRunning() {
        return running.get();
    }

    @Override
    public int getPhase() {
        return 0; // start early with root context
    }

    private void acceptLoop(ServerSocket ss, int port) {
        while (running.get()) {
            try {
                Socket socket = ss.accept();
                if (!connectionSemaphore.tryAcquire()) {
                    safeClose(socket);
                    continue;
                }
                workerPool.submit(() -> handleConnection(socket, port));
            } catch (SocketTimeoutException timeout) {
                // expected
            } catch (IOException e) {
                if (running.get()) {
                    log.warn("Acceptor error on port {}: {}", port, e.toString());
                }
            }
        }
    }

    private void handleConnection(Socket socket, int port) {
        activeConnections.incrementAndGet();
        requestsTotal.increment();
        try (socket) {
            socket.setSoTimeout(props.getSocketReadTimeoutMs());

            String message = readOneFrame(socket);
            if (message == null) return;

            var result = requestDuration.record(() -> processor.handle(message, props.getSlaMs()));
            if (!result.withinSla()) {
                slaViolationsTotal.increment();
            }
            writeResponse(socket, result.response());

        } catch (Exception e) {
            requestsErrors.increment();
            log.debug("Connection error on port {}: {}", port, e.toString());
        } finally {
            activeConnections.decrementAndGet();
            connectionSemaphore.release();
        }
    }

    private String readOneFrame(Socket socket) throws IOException {
        BufferedInputStream in = new BufferedInputStream(socket.getInputStream());

        byte[] buf = new byte[4096];
        StringBuilder sb = new StringBuilder();

        while (true) {
            try {
                int n = in.read(buf);
                if (n == -1) break;
                if (n > 0) sb.append(new String(buf, 0, n, StandardCharsets.US_ASCII));
            } catch (SocketTimeoutException done) {
                break;
            }
        }

        String msg = sb.toString();
        if (msg.isBlank()) return null;
        return msg.replaceAll("[\r\n]+$", "");
    }

    private void writeResponse(Socket socket, String response) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
        out.write(response.getBytes(StandardCharsets.US_ASCII));
        out.flush();
    }

    private void safeClose(Socket s) {
        try { s.close(); } catch (Exception ignored) {}
    }
}
