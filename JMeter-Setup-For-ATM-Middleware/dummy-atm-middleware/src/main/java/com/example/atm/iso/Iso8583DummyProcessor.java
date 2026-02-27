package com.example.atm.iso;

import java.time.Duration;
import java.time.Instant;

public class Iso8583DummyProcessor {

    public ProcessResult handle(String rawMessage, int slaMs) {
        Instant start = Instant.now();

        if (rawMessage == null || rawMessage.isBlank()) {
            return result("ERR_EMPTY", "A4MERR|EMPTY", start, slaMs);
        }
        if (!rawMessage.startsWith("A4M")) {
            return result("ERR_BAD_PREFIX", "A4MERR|BAD_PREFIX", start, slaMs);
        }

        String response = buildResponse(rawMessage);

        Duration took = Duration.between(start, Instant.now());
        boolean withinSla = took.toMillis() <= slaMs;

        if (!withinSla) {
            response = "A4MTIMEOUT|" + response;
        }
        return new ProcessResult("OK", response, took.toMillis(), withinSla);
    }

    private String buildResponse(String req) {
        String r = req;
        r = r.replaceFirst("0100", "0110");
        r = r.replaceFirst("0200", "0210");
        r = r.replaceFirst("0400", "0410");
        if (r.equals(req)) {
            r = req + "|RESP";
        }
        return r;
    }

    private ProcessResult result(String code, String response, Instant start, int slaMs) {
        long tookMs = Duration.between(start, Instant.now()).toMillis();
        return new ProcessResult(code, response, tookMs, tookMs <= slaMs);
    }

    public record ProcessResult(String status, String response, long processingMs, boolean withinSla) {}
}
