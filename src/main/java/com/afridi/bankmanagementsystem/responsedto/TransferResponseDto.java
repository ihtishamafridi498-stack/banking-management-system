package com.afridi.bankmanagementsystem.responsedto;

import java.math.BigDecimal;

public record TransferResponseDto(
        String senderAccountNumber,
        String receiverAccountNumber,
        BigDecimal amount,
        String transactionReference,
        BigDecimal senderUpdatedBalance,
        BigDecimal receiverUpdatedBalance,
        String message
) {
}
