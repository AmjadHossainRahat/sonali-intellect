package com.bank.rtgs.returnmanagement.application.dto;

import com.bank.rtgs.returnmanagement.domain.model.RtgsReturn;
import com.bank.rtgs.returnmanagement.domain.model.ReturnStatus;
import java.time.Instant;
import java.util.UUID;

public record RtgsReturnResponse(
        UUID id,
        String originalTransactionReference,
        String reasonCode,
        String reasonDescription,
        ReturnStatus status,
        String payload,
        Instant createdAtUtc,
        Instant generatedAtUtc,
        Instant sentAtUtc,
        Instant acknowledgedAtUtc
) {
    public static RtgsReturnResponse from(RtgsReturn rtgsReturn) {
        return new RtgsReturnResponse(
                rtgsReturn.getId(),
                rtgsReturn.getOriginalTransactionReference().value(),
                rtgsReturn.getReason().code(),
                rtgsReturn.getReason().description(),
                rtgsReturn.getStatus(),
                rtgsReturn.getPayload(),
                rtgsReturn.getCreatedAtUtc(),
                rtgsReturn.getGeneratedAtUtc(),
                rtgsReturn.getSentAtUtc(),
                rtgsReturn.getAcknowledgedAtUtc()
        );
    }
}
