package com.bank.rtgs.returnmanagement.api;

import com.bank.rtgs.returnmanagement.application.dto.CreateReturnRequest;
import com.bank.rtgs.returnmanagement.application.dto.RtgsReturnResponse;
import com.bank.rtgs.returnmanagement.application.service.RtgsReturnApplicationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/returns")
public class RtgsReturnController {
    private final RtgsReturnApplicationService service;

    public RtgsReturnController(RtgsReturnApplicationService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RtgsReturnResponse create(@Valid @RequestBody CreateReturnApiRequest request) {
        return service.create(new CreateReturnRequest(request.originalTransactionReference(), request.reasonCode(), request.reasonDescription()));
    }

    @PostMapping("/{id}/generate-payload")
    public RtgsReturnResponse generatePayload(@PathVariable UUID id) { return service.generatePayload(id); }

    @PostMapping("/{id}/mark-sent")
    public RtgsReturnResponse markSent(@PathVariable UUID id) { return service.markSent(id); }

    @PostMapping("/{id}/acknowledge")
    public RtgsReturnResponse acknowledge(@PathVariable UUID id) { return service.acknowledge(id); }

    @GetMapping("/{id}")
    public RtgsReturnResponse get(@PathVariable UUID id) { return service.get(id); }

    public record CreateReturnApiRequest(
            @NotBlank String originalTransactionReference,
            @NotBlank String reasonCode,
            @NotBlank String reasonDescription) {}
}
