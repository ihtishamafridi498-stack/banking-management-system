package com.afridi.bankmanagementsystem.serviceimpl;

import com.afridi.bankmanagementsystem.enums.AccountStatus;
import com.afridi.bankmanagementsystem.enums.TransactionStatus;
import com.afridi.bankmanagementsystem.enums.TransactionType;
import com.afridi.bankmanagementsystem.exception.*;
import com.afridi.bankmanagementsystem.model.Account;
import com.afridi.bankmanagementsystem.model.Transaction;
import com.afridi.bankmanagementsystem.repository.AccountRepository;
import com.afridi.bankmanagementsystem.repository.TransactionRepository;
import com.afridi.bankmanagementsystem.requestdto.DepositRequestDto;
import com.afridi.bankmanagementsystem.requestdto.TransferRequestDto;
import com.afridi.bankmanagementsystem.requestdto.WithdrawRequestDto;
import com.afridi.bankmanagementsystem.responsedto.DepositResponseDto;
import com.afridi.bankmanagementsystem.responsedto.TransactionResponseDto;
import com.afridi.bankmanagementsystem.responsedto.TransferResponseDto;
import com.afridi.bankmanagementsystem.responsedto.WithdrawResponseDto;
import com.afridi.bankmanagementsystem.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Override
    @Transactional
    public DepositResponseDto deposit(DepositRequestDto requestDto) {


        Account account = accountRepository.findByAccountNumber(requestDto.accountNumber())
                .orElseThrow(() ->
                        new AccountNotFoundException( "Account with number '" + requestDto.accountNumber() + "' was not found."));

        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountStatusException( "Deposits are allowed only for active accounts.");
        }

        account.setBalance(account.getBalance().add(requestDto.amount()));

        accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setAmount(requestDto.amount());
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setReferenceNumber(generateReferenceNumber());

        transaction.setReceiverAccount(account);

        transactionRepository.save(transaction);

        return new DepositResponseDto(
                account.getAccountNumber(),
                requestDto.amount(),
                account.getBalance(),
                "Amount deposited successfully."
        );
    }

    private String generateReferenceNumber() {
        return "TXN" + System.currentTimeMillis();
    }

    @Override
    @Transactional
    public WithdrawResponseDto withdraw(WithdrawRequestDto requestDto) {

        Account account = accountRepository.findByAccountNumber(requestDto.accountNumber())
                .orElseThrow(() ->
                        new AccountNotFoundException("Account with number '" + requestDto.accountNumber() + "' was not found."));

        if (account.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountStatusException( "Withdrawals are allowed only for active accounts.");
        }

        if (account.getBalance().compareTo(requestDto.amount()) < 0) {
            throw new InsufficientBalanceException( "Account '" + account.getAccountNumber()
                    + "' has insufficient balance to complete this withdrawal.");
        }

        account.setBalance(account.getBalance().subtract(requestDto.amount()));

        accountRepository.save(account);

        Transaction transaction = new Transaction();

        transaction.setAmount(requestDto.amount());
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setReferenceNumber(generateReferenceNumber());

        transaction.setSenderAccount(account);

        transactionRepository.save(transaction);

        return new WithdrawResponseDto(
                account.getAccountNumber(),
                requestDto.amount(),
                account.getBalance(),
                "Amount withdrawn successfully."
        );
    }

    @Override
    @Transactional
    public TransferResponseDto transfer(TransferRequestDto requestDto) {

        Account senderAccount = accountRepository
                .findByAccountNumber(requestDto.senderAccountNumber())
                .orElseThrow(() ->
                        new AccountNotFoundException( "Sender account '" + requestDto.senderAccountNumber() + "' was not found."));

        Account receiverAccount = accountRepository
                .findByAccountNumber(requestDto.receiverAccountNumber())
                .orElseThrow(() ->
                        new AccountNotFoundException( "Receiver account '" + requestDto.receiverAccountNumber() + "' was not found."));

        if (senderAccount.getAccountNumber().equals(receiverAccount.getAccountNumber())) {
            throw new InvalidTransferException( "Sender and receiver accounts must be different.");
        }

        if (senderAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountStatusException( "Money cannot be transferred from an inactive, frozen, or closed sender account.");
        }

        if (receiverAccount.getAccountStatus() != AccountStatus.ACTIVE) {
            throw new InvalidAccountStatusException("Money cannot be transferred to an inactive, frozen, or closed receiver account.");
        }

        if (senderAccount.getBalance().compareTo(requestDto.amount()) < 0) {
            throw new InsufficientBalanceException("Account '" + senderAccount.getAccountNumber()
                    + "' has insufficient balance to complete this transfer.");
        }

        senderAccount.setBalance(
                senderAccount.getBalance().subtract(requestDto.amount())
        );

        receiverAccount.setBalance(
                receiverAccount.getBalance().add(requestDto.amount())
        );

        accountRepository.save(senderAccount);
        accountRepository.save(receiverAccount);

        Transaction transaction = new Transaction();

        transaction.setAmount(requestDto.amount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transaction.setReferenceNumber(generateReferenceNumber());
        transaction.setSenderAccount(senderAccount);
        transaction.setReceiverAccount(receiverAccount);

        transactionRepository.save(transaction);

        return new TransferResponseDto(
                senderAccount.getAccountNumber(),
                receiverAccount.getAccountNumber(),
                requestDto.amount(),
                transaction.getReferenceNumber(),
                senderAccount.getBalance(),
                receiverAccount.getBalance(),
                "Amount transferred successfully."
        );
    }

    @Override
    public TransactionResponseDto getTransactionById(Long transactionId) {

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new TransactionNotFoundException("Transaction with ID " + transactionId + " was not found."));

        return new TransactionResponseDto(
                transaction.getTransactionId(),
                transaction.getAmount(),
                transaction.getTransactionType(),

                transaction.getSenderAccount() != null
                        ? transaction.getSenderAccount().getAccountNumber()
                        : null,

                transaction.getReceiverAccount() != null
                        ? transaction.getReceiverAccount().getAccountNumber()
                        : null,

                transaction.getTransactionStatus(),
                transaction.getReferenceNumber(),
                transaction.getTransactionDate()
        );
    }

    @Override
    public List<TransactionResponseDto> getAllTransactions(){
        List<Transaction> transactions=transactionRepository.findAll();

        List<TransactionResponseDto> responseDtos=new ArrayList<>();

        for(Transaction transaction:transactions){

            TransactionResponseDto dto=new TransactionResponseDto(
                    transaction.getTransactionId(),
                    transaction.getAmount(),
                    transaction.getTransactionType(),

                    transaction.getSenderAccount() != null
                            ? transaction.getSenderAccount().getAccountNumber()
                            : null,

                    transaction.getReceiverAccount() != null
                            ? transaction.getReceiverAccount().getAccountNumber()
                            : null,

                    transaction.getTransactionStatus(),
                    transaction.getReferenceNumber(),
                    transaction.getTransactionDate()
            );
            responseDtos.add(dto);
        }
        return responseDtos;
    }

    @Override
   public List<TransactionResponseDto> getAccountTransactions(String accountNumber){
      List<Transaction> transactions=transactionRepository. findTransactionsByAccountNumber(accountNumber);

        if (transactions.isEmpty()) {
            throw new NoTransactionsFoundException(
                    "No transaction history was found for account '"
                            + accountNumber + "'."
            );
        }

        List<TransactionResponseDto> responseDtos=new ArrayList<>();

        for(Transaction transaction:transactions){

            TransactionResponseDto dto=new TransactionResponseDto(
                    transaction.getTransactionId(),
                    transaction.getAmount(),
                    transaction.getTransactionType(),

                    transaction.getSenderAccount() != null
                            ? transaction.getSenderAccount().getAccountNumber()
                            : null,

                    transaction.getReceiverAccount() != null
                            ? transaction.getReceiverAccount().getAccountNumber()
                            : null,

                    transaction.getTransactionStatus(),
                    transaction.getReferenceNumber(),
                    transaction.getTransactionDate()
            );
            responseDtos.add(dto);
        }
        return responseDtos;
    }

    }
