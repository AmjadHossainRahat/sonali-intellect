package com.bank.rtgs.returnmanagement.domain.model;

import java.util.Objects;

public record TransactionReference(String value) {
    public TransactionReference {
        Objects.requireNonNull(value, "Transaction reference is required.");
        if (value.isBlank()) throw new IllegalArgumentException("Transaction reference is required.");
    }
}
