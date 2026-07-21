package com.afridi.bankmanagementsystem.controller;


import com.afridi.bankmanagementsystem.requestdto.CreateAccountRequestDto;
import com.afridi.bankmanagementsystem.requestdto.UpdateAccountStatusRequestDto;
import com.afridi.bankmanagementsystem.responsedto.AccountResponseDto;
import com.afridi.bankmanagementsystem.responsedto.CreateAccountResponseDto;
import com.afridi.bankmanagementsystem.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping()
    public ResponseEntity<CreateAccountResponseDto> createAccount(@Valid @RequestBody CreateAccountRequestDto requestDto){
        CreateAccountResponseDto responseDto=accountService.createAccount(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDto>> getAllAccounts(){
        List<AccountResponseDto> responseDtos=accountService.getAllAccounts();
        return  ResponseEntity.status(HttpStatus.OK).body(responseDtos);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDto> getAccountByAccountNumber(@PathVariable String accountNumber){
        AccountResponseDto responseDto=accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<AccountResponseDto>> getCustomerAccounts(@PathVariable Long customerId) {
        List<AccountResponseDto> responseDtos = accountService.getCustomerAccounts(customerId);
        return ResponseEntity.ok(responseDtos);
    }
    @PatchMapping("/{accountNumber}/status")
     public ResponseEntity<AccountResponseDto> updateAccountStatus(@PathVariable String accountNumber, @Valid @RequestBody UpdateAccountStatusRequestDto statusRequestDto){
        AccountResponseDto responseDto=accountService.updateAccountStatus(accountNumber,statusRequestDto);
        return ResponseEntity.ok(responseDto);
        }
    }
