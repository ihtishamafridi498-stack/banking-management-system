package com.afridi.bankmanagementsystem.serviceimpl;


import com.afridi.bankmanagementsystem.enums.*;
import com.afridi.bankmanagementsystem.exception.*;
import com.afridi.bankmanagementsystem.model.Account;
import com.afridi.bankmanagementsystem.model.Customer;
import com.afridi.bankmanagementsystem.model.Transaction;
import com.afridi.bankmanagementsystem.repository.AccountRepository;
import com.afridi.bankmanagementsystem.repository.CustomerRepository;
import com.afridi.bankmanagementsystem.repository.TransactionRepository;
import com.afridi.bankmanagementsystem.requestdto.CreateAccountRequestDto;
import com.afridi.bankmanagementsystem.requestdto.UpdateAccountStatusRequestDto;
import com.afridi.bankmanagementsystem.responsedto.AccountResponseDto;
import com.afridi.bankmanagementsystem.responsedto.CreateAccountResponseDto;
import com.afridi.bankmanagementsystem.service.AccountService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionRepository transactionRepository;

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

    @Override
    @Transactional
    public void applyInterestToAllEligibleAccounts() {
        List<Account> eligibleAccounts = accountRepository
                .findByAccountTypeAndAccountStatus(AccountType.SAVINGS, AccountStatus.ACTIVE);

        log.info("Interest accrual job started. {} eligible accounts found.", eligibleAccounts.size());

        int processed = 0;
        BigDecimal totalInterestCredited = BigDecimal.ZERO;

        for (Account account : eligibleAccounts) {
            if (account.getInterestRate() == null || account.getBalance() == null) {
                continue;
            }

            BigDecimal interest = account.getBalance()
                    .multiply(account.getInterestRate())
                    .setScale(2, RoundingMode.HALF_UP);

            if (interest.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            account.setBalance(account.getBalance().add(interest));
            account.setLastInterestCreditedAt(LocalDateTime.now());
            accountRepository.save(account);

            Transaction transaction = new Transaction();
            transaction.setReceiverAccount(account);
            transaction.setSenderAccount(null);
            transaction.setAmount(interest);
            transaction.setTransactionType(TransactionType.INTEREST);
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
            transaction.setReferenceNumber("INT-" + account.getAccountNumber() + "-" + System.currentTimeMillis());
            transactionRepository.save(transaction);

            processed++;
            totalInterestCredited = totalInterestCredited.add(interest);
        }

        log.info("Interest accrual job finished. {} accounts credited, total interest credited: {}",
                processed, totalInterestCredited);
    }
}