package com.afridi.bankmanagementsystem.responsedto;

public record CloseAccountResponseDto(
        String accountNumber,
        String status,
        String message
) {
}
