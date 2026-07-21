package com.afridi.bankmanagementsystem.requestdto;

import com.afridi.bankmanagementsystem.enums.AccountType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateAccountRequestDto(

        @NotNull(message = "Customer ID is required")
                Long customerId,

        @NotNull(message = "Account type is required")
        AccountType accountType,

        @NotNull(message = "Initial deposit is required")
        @DecimalMin(value = "1000.00", inclusive = true,
                message = "Initial deposit must be at least 1000")
        BigDecimal initialDeposit
) {
}
