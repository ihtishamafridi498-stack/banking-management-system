package com.afridi.bankmanagementsystem.requestdto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record WithdrawRequestDto(

        @NotBlank(message = "Account number is required")
        String accountNumber,

        @DecimalMin(value = "0.0", inclusive = false,
                message = "Amount must be greater than zero")
        BigDecimal amount
) {
}
