package com.afridi.bankmanagementsystem.service;

import com.afridi.bankmanagementsystem.requestdto.CreateAccountRequestDto;
import com.afridi.bankmanagementsystem.requestdto.UpdateAccountStatusRequestDto;
import com.afridi.bankmanagementsystem.responsedto.AccountResponseDto;
import com.afridi.bankmanagementsystem.responsedto.CreateAccountResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface AccountService {
    CreateAccountResponseDto createAccount( CreateAccountRequestDto requestDto);

    List<AccountResponseDto> getAllAccounts();

    AccountResponseDto getAccountByAccountNumber(String accountNumber);

    List<AccountResponseDto> getCustomerAccounts(Long customerId);

    AccountResponseDto updateAccountStatus(String accountNumber, UpdateAccountStatusRequestDto statusRequestDto);
}
