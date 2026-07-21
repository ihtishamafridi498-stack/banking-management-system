package com.afridi.bankmanagementsystem.responsedto;

import java.math.BigDecimal;

public record WithdrawResponseDto(
        String accountNumber,
        BigDecimal withdrawnAmount,
        BigDecimal updatedBalance,
        String message
) {
}
