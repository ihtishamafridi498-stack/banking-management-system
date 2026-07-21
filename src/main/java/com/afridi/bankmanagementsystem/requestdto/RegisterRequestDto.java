package com.afridi.bankmanagementsystem.requestdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 20,
                message = "Username must be between 3 and 20 characters")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8,
                message = "Password must be at least 8 characters")
        String password,

        @NotBlank(message = "Customer name is required")
        String customerName,

        @NotBlank(message = "Phone number is required")
        String phone,

        @NotBlank(message = "CNIC is required")
        @Pattern(
                regexp = "\\d{5}-\\d{7}-\\d",
                message = "CNIC must be in format 12345-1234567-1"
        )
        String customerCnic,

        @NotBlank(message = "Address is required")
        String address
) {
}