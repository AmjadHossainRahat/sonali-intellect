package com.bank.rtgs.returnmanagement.infrastructure.payload;

import com.bank.rtgs.returnmanagement.domain.model.RtgsReturn;
import com.bank.rtgs.returnmanagement.domain.ports.ReturnPayloadGenerator;
import org.springframework.stereotype.Component;

@Component
public class SimpleReturnPayloadGenerator implements ReturnPayloadGenerator {
    @Override
    public String generate(RtgsReturn rtgsReturn) {
        return "<RtgsReturn>" +
                "<ReturnId>" + rtgsReturn.getId() + "</ReturnId>" +
                "<OriginalTransactionReference>" + rtgsReturn.getOriginalTransactionReference().value() + "</OriginalTransactionReference>" +
                "<ReasonCode>" + rtgsReturn.getReason().code() + "</ReasonCode>" +
                "<ReasonDescription>" + rtgsReturn.getReason().description() + "</ReasonDescription>" +
                "</RtgsReturn>";
    }
}
