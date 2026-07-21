package com.afridi.bankmanagementsystem.service;

import com.afridi.bankmanagementsystem.requestdto.DepositRequestDto;
import com.afridi.bankmanagementsystem.requestdto.TransferRequestDto;
import com.afridi.bankmanagementsystem.requestdto.WithdrawRequestDto;
import com.afridi.bankmanagementsystem.responsedto.DepositResponseDto;
import com.afridi.bankmanagementsystem.responsedto.TransactionResponseDto;
import com.afridi.bankmanagementsystem.responsedto.TransferResponseDto;
import com.afridi.bankmanagementsystem.responsedto.WithdrawResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface TransactionService {
    DepositResponseDto deposit(DepositRequestDto requestDto);

    WithdrawResponseDto withdraw( WithdrawRequestDto requestDto);


    TransferResponseDto transfer(TransferRequestDto requestDto);

    TransactionResponseDto getTransactionById(Long transactionId);

    List<TransactionResponseDto> getAllTransactions();

    List<TransactionResponseDto> getAccountTransactions(String accountNumber);
}
