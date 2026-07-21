package com.afridi.bankmanagementsystem.requestdto;

import jakarta.validation.constraints.NotBlank;

public record FreezeAccountRequestDto(
        @NotBlank(message = "Account number is required")
        String accountNumber
) {
}
