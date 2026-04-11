package com.bank.rtgs.returnmanagement.infrastructure.persistence;

import com.bank.rtgs.returnmanagement.domain.model.RtgsReturn;
import com.bank.rtgs.returnmanagement.domain.ports.RtgsReturnRepository;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryRtgsReturnRepository implements RtgsReturnRepository {
    private final Map<UUID, RtgsReturn> store = new ConcurrentHashMap<>();

    @Override
    public RtgsReturn save(RtgsReturn rtgsReturn) {
        store.put(rtgsReturn.getId(), rtgsReturn);
        return rtgsReturn;
    }

    @Override
    public Optional<RtgsReturn> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }
}
