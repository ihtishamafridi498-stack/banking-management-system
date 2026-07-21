package com.afridi.bankmanagementsystem.responsedto;

public record RegisterResponseDto(

        Long userId,
        Long customerId,
        String username,
        String email,
        String role,
        String message
) {
}