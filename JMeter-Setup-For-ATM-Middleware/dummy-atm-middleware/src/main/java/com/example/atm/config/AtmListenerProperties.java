package com.example.atm.config;

import java.util.ArrayList;
import java.util.List;

public class AtmListenerProperties {
    private List<Integer> ports = new ArrayList<>();
    private int acceptTimeoutMs = 2000;
    private int socketReadTimeoutMs = 1500;
    private int slaMs = 10000;
    private int workerThreads = 32;
    private int maxConnections = 200;

    public List<Integer> getPorts() { return ports; }
    public void setPorts(List<Integer> ports) { this.ports = ports; }

    public int getAcceptTimeoutMs() { return acceptTimeoutMs; }
    public void setAcceptTimeoutMs(int acceptTimeoutMs) { this.acceptTimeoutMs = acceptTimeoutMs; }

    public int getSocketReadTimeoutMs() { return socketReadTimeoutMs; }
    public void setSocketReadTimeoutMs(int socketReadTimeoutMs) { this.socketReadTimeoutMs = socketReadTimeoutMs; }

    public int getSlaMs() { return slaMs; }
    public void setSlaMs(int slaMs) { this.slaMs = slaMs; }

    public int getWorkerThreads() { return workerThreads; }
    public void setWorkerThreads(int workerThreads) { this.workerThreads = workerThreads; }

    public int getMaxConnections() { return maxConnections; }
    public void setMaxConnections(int maxConnections) { this.maxConnections = maxConnections; }
}
