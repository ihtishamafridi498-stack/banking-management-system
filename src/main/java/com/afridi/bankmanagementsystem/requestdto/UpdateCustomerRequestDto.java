package com.afridi.bankmanagementsystem.requestdto;

import jakarta.validation.constraints.NotBlank;

public record UpdateCustomerRequestDto(
        @NotBlank(message = "Customer name is required")
        String customerName,

        @NotBlank(message = "Phone number is required")
        String phone,

        @NotBlank(message = "CNIC is required")
        String customerCnic,

        @NotBlank(message = "Address is required")
        String address
) {
}
