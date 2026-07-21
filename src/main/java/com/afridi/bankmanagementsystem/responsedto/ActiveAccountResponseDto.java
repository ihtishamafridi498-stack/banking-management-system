package com.afridi.bankmanagementsystem.responsedto;

public record ActiveAccountResponseDto(

        String accountNumber,
        String status,
        String message
) {
}
