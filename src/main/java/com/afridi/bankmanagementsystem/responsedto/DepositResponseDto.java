package com.afridi.bankmanagementsystem.responsedto;

import java.math.BigDecimal;

public record DepositResponseDto(
        String accountNumber,
        BigDecimal depositedAmount,
        BigDecimal updatedBalance,
        String message
) {
}
