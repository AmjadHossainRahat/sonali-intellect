package com.bank.rtgs.returnmanagement.domain.model;

import java.time.Instant;
import java.util.UUID;

public class RtgsReturn {
    private final UUID id;
    private final TransactionReference originalTransactionReference;
    private final ReturnReason reason;
    private ReturnStatus status;
    private String payload;
    private final Instant createdAtUtc;
    private Instant generatedAtUtc;
    private Instant sentAtUtc;
    private Instant acknowledgedAtUtc;

    public RtgsReturn(TransactionReference originalTransactionReference, ReturnReason reason, Instant createdAtUtc) {
        this.id = UUID.randomUUID();
        this.originalTransactionReference = originalTransactionReference;
        this.reason = reason;
        this.createdAtUtc = createdAtUtc;
        this.status = ReturnStatus.DRAFT;
    }

    public void generatePayload(String payload, Instant generatedAtUtc) {
        if (status != ReturnStatus.DRAFT) throw new IllegalStateException("Payload can only be generated from DRAFT status.");
        if (payload == null || payload.isBlank()) throw new IllegalArgumentException("Payload is required.");
        this.payload = payload;
        this.generatedAtUtc = generatedAtUtc;
        this.status = ReturnStatus.GENERATED;
    }

    public void markAsSent(Instant sentAtUtc) {
        if (status != ReturnStatus.GENERATED) throw new IllegalStateException("Return can only be sent after payload generation.");
        this.sentAtUtc = sentAtUtc;
        this.status = ReturnStatus.SENT;
    }

    public void acknowledge(Instant acknowledgedAtUtc) {
        if (status != ReturnStatus.SENT) throw new IllegalStateException("Only SENT returns can be acknowledged.");
        this.acknowledgedAtUtc = acknowledgedAtUtc;
        this.status = ReturnStatus.ACKNOWLEDGED;
    }

    public UUID getId() { return id; }
    public TransactionReference getOriginalTransactionReference() { return originalTransactionReference; }
    public ReturnReason getReason() { return reason; }
    public ReturnStatus getStatus() { return status; }
    public String getPayload() { return payload; }
    public Instant getCreatedAtUtc() { return createdAtUtc; }
    public Instant getGeneratedAtUtc() { return generatedAtUtc; }
    public Instant getSentAtUtc() { return sentAtUtc; }
    public Instant getAcknowledgedAtUtc() { return acknowledgedAtUtc; }
}
