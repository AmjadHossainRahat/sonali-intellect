package com.bank.rtgs.returnmanagement.domain.ports;

import com.bank.rtgs.returnmanagement.domain.model.RtgsReturn;
import java.util.Optional;
import java.util.UUID;

public interface RtgsReturnRepository {
    RtgsReturn save(RtgsReturn rtgsReturn);
    Optional<RtgsReturn> findById(UUID id);
}
