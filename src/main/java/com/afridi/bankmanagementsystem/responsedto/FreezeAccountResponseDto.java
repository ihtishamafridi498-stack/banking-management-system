package com.afridi.bankmanagementsystem.responsedto;

public record FreezeAccountResponseDto(
        String accountNumber,
        String status,
        String message
) {
}
