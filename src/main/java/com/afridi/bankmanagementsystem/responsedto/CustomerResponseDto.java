package com.afridi.bankmanagementsystem.responsedto;

import com.afridi.bankmanagementsystem.enums.CustomerStatus;

public record CustomerResponseDto(
        Long customerId,
        String customerName,
        String customerEmail,
        String phone,
        String customerCnic,
        String address,
        CustomerStatus status
) {
}
