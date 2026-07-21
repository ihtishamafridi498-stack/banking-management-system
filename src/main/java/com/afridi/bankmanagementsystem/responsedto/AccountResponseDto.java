package com.afridi.bankmanagementsystem.responsedto;

import com.afridi.bankmanagementsystem.enums.AccountStatus;
import com.afridi.bankmanagementsystem.enums.AccountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AccountResponseDto(
        Long accountId,
        String accountNumber,
        BigDecimal balance,
        AccountType accountType,
        AccountStatus accountStatus,
        LocalDateTime createdAt,
        Long customerId
) {
}
