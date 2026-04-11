package com.bank.rtgs.returnmanagement.application.dto;

public record CreateReturnRequest(String originalTransactionReference, String reasonCode, String reasonDescription) {}
