package com.afridi.bankmanagementsystem.responsedto;

public record UserResponseDto(
        Long userId,
        String userName,
        String email,
        String role
) {
}
