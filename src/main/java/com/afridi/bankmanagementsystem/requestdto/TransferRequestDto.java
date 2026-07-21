package com.afridi.bankmanagementsystem.requestdto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record TransferRequestDto(

        @NotBlank(message = "Sender account number is required")
        String senderAccountNumber,

        @NotBlank(message = "Receiver account number is required")
        String receiverAccountNumber,

        @DecimalMin(value = "0.0", inclusive = false,
                message = "Amount must be greater than zero")
        BigDecimal amount
) {
}
