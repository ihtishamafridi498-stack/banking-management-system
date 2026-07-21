package com.afridi.bankmanagementsystem.controller;

import com.afridi.bankmanagementsystem.requestdto.DepositRequestDto;
import com.afridi.bankmanagementsystem.requestdto.TransferRequestDto;
import com.afridi.bankmanagementsystem.requestdto.WithdrawRequestDto;
import com.afridi.bankmanagementsystem.responsedto.DepositResponseDto;
import com.afridi.bankmanagementsystem.responsedto.TransactionResponseDto;
import com.afridi.bankmanagementsystem.responsedto.TransferResponseDto;
import com.afridi.bankmanagementsystem.responsedto.WithdrawResponseDto;
import com.afridi.bankmanagementsystem.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;


    @PostMapping("/deposit")
    public ResponseEntity<DepositResponseDto> deposit(
            @Valid @RequestBody DepositRequestDto requestDto) {

        DepositResponseDto responseDto = transactionService.deposit(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


   @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponseDto> withdraw(
            @Valid @RequestBody WithdrawRequestDto requestDto) {

        WithdrawResponseDto responseDto = transactionService.withdraw(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @PostMapping("/transfer")
    public ResponseEntity<TransferResponseDto > transfer(
            @Valid @RequestBody TransferRequestDto requestDto) {

        TransferResponseDto responseDto = transactionService.transfer(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }


    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(
            @PathVariable Long transactionId) {

        TransactionResponseDto responseDto =
                transactionService.getTransactionById(transactionId);

        return ResponseEntity.ok(responseDto);
    }


    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions() {

        List<TransactionResponseDto> responseDtos =
                transactionService.getAllTransactions();

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<TransactionResponseDto>> getAccountTransactions(
            @PathVariable String accountNumber) {

        List<TransactionResponseDto> responseDtos =
                transactionService.getAccountTransactions(accountNumber);

        return ResponseEntity.ok(responseDtos);
    }
}

