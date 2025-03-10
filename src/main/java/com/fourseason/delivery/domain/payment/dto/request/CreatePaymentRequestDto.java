package com.fourseason.delivery.domain.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record CreatePaymentRequestDto(

        @NotNull(message = "주문id는 필수 입력 값입니다.")
        UUID orderId,

        @NotBlank(message = "결제승인키는 필수 입력 값입니다.")
        String paymentKey,

        @PositiveOrZero(message = "결제금액은 음수가 될 수 없습니다.")
        int amount

) {

}