package com.afridi.bankmanagementsystem.requestdto;

import com.afridi.bankmanagementsystem.enums.CustomerStatus;

public record CustomerStatusUpdateRequestDto(
        CustomerStatus status,
        String reason
) {
}
