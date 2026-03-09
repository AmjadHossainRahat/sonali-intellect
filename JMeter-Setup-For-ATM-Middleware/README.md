# Dummy ATM Middleware (Spring Framework, NOT Spring Boot)

This is a **plain Spring Framework** (non-Boot) **Spring MVC** web application packaged as a **WAR**.
It also starts **multi-port TCP socket listeners** (ServerSocket) for ISO-like traffic simulation.

## What you get
- HTTP endpoint (Spring MVC): `GET http://localhost:8080/api/ping`
- TCP listeners on configurable ports (default): `9101,9102,9103`
- Dummy protocol:
  - Request must start with `A4M`
  - MTI transforms: `0100->0110`, `0200->0210`, `0400->0410`, else appends `|RESP`

## Prerequisites
- Java 17+
- Maven 3.9+

## Option A: Run locally with Jetty (recommended)
This uses `jetty-maven-plugin` to run the WAR without Spring Boot.

```bash
mvn -q jetty:run
```

Open:
- `http://localhost:8080/api/ping`

## Option B: Build WAR and deploy to Tomcat 10+
Build:
```bash
mvn -q package
```

WAR output:
- `target/atm-middleware-spring-mvc.war`

Deploy the WAR to **Tomcat 10+** (Jakarta Servlet 6).

## Configure TCP Listener Ports
Edit:
- `src/main/resources/atm-listeners.properties`

Example for 20 ports:
```properties
atm.listeners.ports=9101,9102,9103,9104,9105,9106,9107,9108,9109,9110,9111,9112,9113,9114,9115,9116,9117,9118,9119,9120
```

## Confihure jMetere: [JMeterConfiguration-README.md](JMeterConfiguration-README.md)


## Quick TCP Manual Test

### Linux/macOS
```bash
printf "A4M100000200HELLO" | nc -w 2 127.0.0.1 9101
```

### Windows PowerShell
```powershell
$client = New-Object System.Net.Sockets.TcpClient("localhost", 9101)
$stream = $client.GetStream()
$writer = New-Object System.IO.StreamWriter($stream)
$writer.AutoFlush = $true
$writer.Write("A4M100000200HELLO")
Start-Sleep -Milliseconds 200
$buffer = New-Object byte[] 4096
$read = $stream.Read($buffer, 0, $buffer.Length)
[System.Text.Encoding]::ASCII.GetString($buffer, 0, $read)
$client.Close()
```
Expected response contains `0210`.


## Metrics (Prometheus format)
This project includes **Micrometer + Prometheus registry** and exposes:

- `GET http://localhost:8080/metrics`

You can scrape this endpoint from Prometheus.

Key custom metrics:
- `atm_socket_requests_total`
- `atm_socket_requests_errors_total`
- `atm_socket_sla_violations_total`
- `atm_socket_request_duration_seconds` (with p50/p95/p99)
- `atm_socket_active_connections`

