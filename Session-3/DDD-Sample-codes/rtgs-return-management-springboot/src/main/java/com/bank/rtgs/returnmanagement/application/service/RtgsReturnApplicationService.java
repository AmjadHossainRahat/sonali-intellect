package com.bank.rtgs.returnmanagement.application.service;

import com.bank.rtgs.returnmanagement.application.dto.CreateReturnRequest;
import com.bank.rtgs.returnmanagement.application.dto.RtgsReturnResponse;
import com.bank.rtgs.returnmanagement.domain.model.ReturnReason;
import com.bank.rtgs.returnmanagement.domain.model.RtgsReturn;
import com.bank.rtgs.returnmanagement.domain.model.TransactionReference;
import com.bank.rtgs.returnmanagement.domain.ports.ReturnPayloadGenerator;
import com.bank.rtgs.returnmanagement.domain.ports.RtgsReturnRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RtgsReturnApplicationService {
    private final RtgsReturnRepository repository;
    private final ReturnPayloadGenerator payloadGenerator;

    public RtgsReturnApplicationService(RtgsReturnRepository repository, ReturnPayloadGenerator payloadGenerator) {
        this.repository = repository;
        this.payloadGenerator = payloadGenerator;
    }

    public RtgsReturnResponse create(CreateReturnRequest request) {
        RtgsReturn rtgsReturn = new RtgsReturn(
                new TransactionReference(request.originalTransactionReference()),
                new ReturnReason(request.reasonCode(), request.reasonDescription()),
                Instant.now()
        );
        repository.save(rtgsReturn);
        return RtgsReturnResponse.from(rtgsReturn);
    }

    public RtgsReturnResponse generatePayload(UUID id) {
        RtgsReturn rtgsReturn = getAggregate(id);
        String payload = payloadGenerator.generate(rtgsReturn);
        rtgsReturn.generatePayload(payload, Instant.now());
        repository.save(rtgsReturn);
        return RtgsReturnResponse.from(rtgsReturn);
    }

    public RtgsReturnResponse markSent(UUID id) {
        RtgsReturn rtgsReturn = getAggregate(id);
        rtgsReturn.markAsSent(Instant.now());
        repository.save(rtgsReturn);
        return RtgsReturnResponse.from(rtgsReturn);
    }

    public RtgsReturnResponse acknowledge(UUID id) {
        RtgsReturn rtgsReturn = getAggregate(id);
        rtgsReturn.acknowledge(Instant.now());
        repository.save(rtgsReturn);
        return RtgsReturnResponse.from(rtgsReturn);
    }

    public RtgsReturnResponse get(UUID id) {
        return RtgsReturnResponse.from(getAggregate(id));
    }

    private RtgsReturn getAggregate(UUID id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("RTGS Return not found: " + id));
    }
}
