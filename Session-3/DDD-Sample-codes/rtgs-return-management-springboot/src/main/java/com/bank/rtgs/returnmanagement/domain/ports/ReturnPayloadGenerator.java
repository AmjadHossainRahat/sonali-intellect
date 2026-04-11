package com.bank.rtgs.returnmanagement.domain.ports;

import com.bank.rtgs.returnmanagement.domain.model.RtgsReturn;

public interface ReturnPayloadGenerator {
    String generate(RtgsReturn rtgsReturn);
}
