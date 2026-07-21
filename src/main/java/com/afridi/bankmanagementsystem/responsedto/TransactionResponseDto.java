package com.afridi.bankmanagementsystem.responsedto;

import com.afridi.bankmanagementsystem.enums.TransactionStatus;
import com.afridi.bankmanagementsystem.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDto(

        Long transactionId,
        BigDecimal amount,
        TransactionType transactionType,
        String senderAccountNumber,
        String receiverAccountNumber,
        TransactionStatus transactionStatus,
        String referenceNumber,
        LocalDateTime transactionDate
) {
}
