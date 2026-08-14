package com.afridi.bankmanagementsystem.serviceimpl;

import com.afridi.bankmanagementsystem.enums.AccountStatus;
import com.afridi.bankmanagementsystem.enums.CustomerStatus;
import com.afridi.bankmanagementsystem.exception.*;
import com.afridi.bankmanagementsystem.model.Account;
import com.afridi.bankmanagementsystem.model.Customer;
import com.afridi.bankmanagementsystem.repository.AccountRepository;
import com.afridi.bankmanagementsystem.repository.CustomerRepository;
import com.afridi.bankmanagementsystem.requestdto.CreateAccountRequestDto;
import com.afridi.bankmanagementsystem.requestdto.UpdateAccountStatusRequestDto;
import com.afridi.bankmanagementsystem.responsedto.AccountResponseDto;
import com.afridi.bankmanagementsystem.responsedto.CreateAccountResponseDto;
import com.afridi.bankmanagementsystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    public CreateAccountResponseDto createAccount(CreateAccountRequestDto requestDto) {

        Customer customer = customerRepository.findById(requestDto.customerId())
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer with ID " + requestDto.customerId() + " was not found."));

        if (customer.getCustomerStatus() != CustomerStatus.ACTIVE) {
            throw new CustomerInActiveException( "Customer with ID " + customer.getCustomerId() +
                    " is inactive and cannot open a new account.");
        }

        Account account = new Account();

        account.setAccountNumber(generateAccountNumber());
        account.setBalance(requestDto.initialDeposit());
        account.setAccountType(requestDto.accountType());
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setCustomer(customer);


        Account savedAccount = accountRepository.save(account);

        return new CreateAccountResponseDto(
                savedAccount.getAccountId(),
                savedAccount.getAccountNumber(),
                savedAccount.getBalance(),
                savedAccount.getAccountType(),
                savedAccount.getAccountStatus(),
                savedAccount.getCreatedAt()
        );
    }

    private String generateAccountNumber() {
        return "ACC" + System.currentTimeMillis();
    }

    @Override
    public List<AccountResponseDto> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        List<AccountResponseDto> responseDtos = new ArrayList<>();

        for (Account account : accounts) {
            AccountResponseDto dto = new AccountResponseDto(
                    account.getAccountId(),
                    account.getAccountNumber(),
                    account.getBalance(),
                    account.getAccountType(),
                    account.getAccountStatus(),
                    account.getCreatedAt(),
                    account.getCustomer().getCustomerId()
            );
            responseDtos.add(dto);
        }
        return responseDtos;
    }

    @Override
    public AccountResponseDto getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException( "Account with number '" + accountNumber + "' was not found."));
        return new AccountResponseDto(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getAccountStatus(),
                account.getCreatedAt(),
                account.getCustomer().getCustomerId()
        );
    }

    @Override
    public List<AccountResponseDto> getCustomerAccounts(Long customerId) {

    Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer with ID " + customerId + " was not found."));

        List<Account> accounts = accountRepository.findByCustomerCustomerId(customerId);

        if (accounts.isEmpty()) {
            throw new NoAccountsFoundException(  "No accounts were found for customer with ID " + customerId + ".");
        }

        List<AccountResponseDto> responseDtos = new ArrayList<>();

        for (Account account : accounts) {
            AccountResponseDto dto = new AccountResponseDto(
                    account.getAccountId(),
                    account.getAccountNumber(),
                    account.getBalance(),
                    account.getAccountType(),
                    account.getAccountStatus(),
                    account.getCreatedAt(),
                    account.getCustomer().getCustomerId()
            );
            responseDtos.add(dto);
        }
        return responseDtos;
    }

    @Override
  public  AccountResponseDto updateAccountStatus(String accountNumber, UpdateAccountStatusRequestDto statusRequestDto){

        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new AccountNotFoundException("Account with number '" + accountNumber + "' was not found."));

        if (account.getAccountStatus() == statusRequestDto.status()) {
            throw new AccountAlreadyInStatusException(  "Account '" + accountNumber +
                    "' is already in '" + statusRequestDto.status() + "' status.");
        }

        if (statusRequestDto.status() == AccountStatus.CLOSED
                && account.getBalance().compareTo(BigDecimal.ZERO) != 0) {

            throw new AccountBalanceNotZeroException( "Account '" + accountNumber +
                    "' cannot be closed because its balance is not zero.");
        }

        if (account.getAccountStatus() == AccountStatus.CLOSED) {
            throw new AccountAlreadyClosedException( "Account '" + accountNumber +
                    "' is closed and cannot be modified.");
        }

        account.setAccountStatus(statusRequestDto.status());

        Account updatedAccount = accountRepository.save(account);

        return new AccountResponseDto(
                updatedAccount.getAccountId(),
                updatedAccount.getAccountNumber(),
                updatedAccount.getBalance(),
                updatedAccount.getAccountType(),
                updatedAccount.getAccountStatus(),
                updatedAccount.getCreatedAt(),
                updatedAccount.getCustomer().getCustomerId()
        );
    }
}