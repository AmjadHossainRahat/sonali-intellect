package com.bank.rtgs.returnmanagement.domain.model;

import java.util.Objects;

public record ReturnReason(String code, String description) {
    public ReturnReason {
        Objects.requireNonNull(code, "Reason code is required.");
        Objects.requireNonNull(description, "Reason description is required.");
        if (code.isBlank()) throw new IllegalArgumentException("Reason code is required.");
        if (description.isBlank()) throw new IllegalArgumentException("Reason description is required.");
    }
}
