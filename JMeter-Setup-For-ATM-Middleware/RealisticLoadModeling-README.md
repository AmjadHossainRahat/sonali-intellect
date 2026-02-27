# Realistic Load Modeling: One Branch vs Whole Bank (JMeter TCP Tests)

This guide shows a realistic way to simulate:
- **one branch** workload
- **many branches / whole bank** workload

It also explains how to read **JMeter Summary Report** and **Aggregate Report** and what to learn from them.

---

## 0) Simple Concepts

### What a “thread” means
A **thread** = one virtual user (think: one ATM terminal or one teller user).

### What “Ramp-Up” means
Ramp-up = how slowly JMeter starts the threads.
- Small ramp-up = sudden spike
- Large ramp-up = gradual start

### What a “Timer” does
Timers control how often each thread sends requests.
Without timers, threads will send requests as fast as possible (stress test, not realistic).

---

## 1) Scenario A — One Branch (Normal Hour)

**Example assumption**
- 8 tellers + 2 ATMs + 2 other sources = **12 concurrent sources**
- Each sends ~1 transaction every ~3 seconds (steady)

### JMeter setup
**Thread Group**
- Number of Threads (users): **12**
- Ramp-Up Period: **5 seconds**
- Loop Count: **Forever** (or a large number like 100000)

**Add Constant Timer**
- Delay: **3000 ms**

### What this means
- 12 virtual users
- each sends 1 request about every 3 seconds
- Approx TPS ≈ 12 / 3 = **~4 TPS**

---

## 2) Scenario B — One Branch (Batch Spike)

**Example assumption**
- One branch posts a batch very quickly (burst traffic)

### JMeter setup
**Thread Group**
- Threads: **200**
- Ramp-Up: **1 second**
- Loop Count: **5**

**Timer**
- No timer (or very small timer like 50–100 ms)

### What this means
- sudden traffic spike
- good for testing saturation, timeouts, limits

---

## 3) Scenario C — 20 Branches (Regional Load)

If **one branch ≈ 12 threads**, then:
- 20 branches ≈ 12 × 20 = **240 threads**

### JMeter setup
**Thread Group**
- Threads: **240**
- Ramp-Up: **20 seconds**
- Loop Count: Forever (or large)

**Constant Timer**
- 3000 ms

Approx TPS ≈ 240 / 3 = **~80 TPS**

---

## 4) Scenario D — Whole Bank (Peak Hour)

Whole-bank load is usually a mix of:
- branch steady traffic
- ATM traffic
- occasional batch spikes

### Practical approach: multiple Thread Groups

1) **Branch Steady Load**
- Threads: (branches × 12)
- Timer: 2000–5000 ms

2) **ATM Steady Load**
- Threads: (ATMs × 1–2)
- Timer: 1000–4000 ms (ATMs are more “random”)

3) **Batch Spike**
- Threads: 100–500
- Ramp-Up: 1–5 seconds
- Loop Count: small (1–10)

Run them together to simulate “whole bank” behavior.

---

## 5) TCP Sampler note for your middleware

Your server reads until **socket timeout OR connection close**.

Best stable option in JMeter:
- **Re-use connection**: unchecked (JMeter auto closes the connection after each request)

This prevents hanging connections.

---

## 6) How to Read Summary Report / Aggregate Report

Both show similar metrics. Aggregate Report is usually easier for comparison.

### Label
Sampler name (example: `TCP-9101`).
Use clear labels if you test multiple ports.

### # Samples
Total number of requests sent.
Example:
- 12 threads × 100 loops = ~1200 samples

### Average (ms)
Average response time.
Important: average can hide slow requests. Do not rely only on it.

### Min / Max
Fastest and slowest response.
- High **Max** usually means timeouts, stalls, or saturation.

### Std. Dev.
How “unstable” the response time is.
- Higher Std. Dev. = more variability (less predictable).

### Error %
% of failed requests (timeouts, connection refused, etc.)
Banking goal is typically close to **0%**.

### Throughput
Requests per second (TPS/RPS).
This is your main “load achieved” number.

How to interpret:
- If you increase threads but Throughput stops increasing → system is saturated.

### Received KB/sec / Sent KB/sec, Avg. Bytes
Network/size information.
Mostly useful to confirm:
- you are receiving responses
- payload size stays consistent

---

## 7) What to conclude (banking-oriented, SLA 10 seconds)

### Can one branch run safely?
Run Scenario A and confirm:
- Error% ~ 0%
- Max << 10,000 ms
- Throughput stable (not dropping)

### Find the capacity limit
Increase step-by-step:
- 1 branch → 5 → 10 → 20 → 50 → ...

At each step record:
- Throughput
- Error%
- Max response time
- Std Dev

Saturation signs:
- Throughput stops increasing
- Error% increases
- Max becomes very high (timeouts)
- Std Dev increases sharply

That point is your current capacity.

---

End of guide.
