package com.fourseason.delivery.domain.payment.dto.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ExternalCancelPaymentDto (
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        UUID orderId,

        String paymentKey,

        int amount,

        String method,

        String status,

        List<Cancel> cancels,

        int balanceAmount
){
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Cancel (
            String cancelReason,
            int cancelAmount
    ){
    }
}
