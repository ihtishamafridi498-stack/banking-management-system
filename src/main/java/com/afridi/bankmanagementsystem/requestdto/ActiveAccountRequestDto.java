package com.afridi.bankmanagementsystem.requestdto;

import jakarta.validation.constraints.NotBlank;

public record ActiveAccountRequestDto(
        @NotBlank(message = "Account number is required")
        String accountNumber
) {
}
