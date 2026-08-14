package com.afridi.bankmanagementsystem.responsedto;

public record LoginResponseDto(

        Long userId,
        String username,
        String role,
        String message,
        String token
) {
}