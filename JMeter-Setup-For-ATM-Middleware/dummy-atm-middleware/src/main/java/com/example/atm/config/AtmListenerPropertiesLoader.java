package com.example.atm.config;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;

public final class AtmListenerPropertiesLoader {

    private AtmListenerPropertiesLoader() {}

    public static AtmListenerProperties load() {
        Properties p = new Properties();
        try (InputStream in = new ClassPathResource("atm-listeners.properties").getInputStream()) {
            p.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load atm-listeners.properties", e);
        }

        AtmListenerProperties props = new AtmListenerProperties();

        String ports = p.getProperty("atm.listeners.ports", "9101,9102,9103");
        props.setPorts(Arrays.stream(ports.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(Integer::parseInt)
                .collect(Collectors.toList()));

        props.setAcceptTimeoutMs(intProp(p, "atm.listeners.acceptTimeoutMs", 2000));
        props.setSocketReadTimeoutMs(intProp(p, "atm.listeners.socketReadTimeoutMs", 1500));
        props.setSlaMs(intProp(p, "atm.listeners.slaMs", 10000));
        props.setWorkerThreads(intProp(p, "atm.listeners.workerThreads", 32));
        props.setMaxConnections(intProp(p, "atm.listeners.maxConnections", 200));

        return props;
    }

    private static int intProp(Properties p, String key, int def) {
        String v = p.getProperty(key);
        if (v == null || v.isBlank()) return def;
        return Integer.parseInt(v.trim());
    }
}
