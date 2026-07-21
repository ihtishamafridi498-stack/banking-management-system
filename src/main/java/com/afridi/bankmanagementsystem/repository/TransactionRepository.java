package com.afridi.bankmanagementsystem.repository;

import com.afridi.bankmanagementsystem.model.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("""
       SELECT t
       FROM Transaction t
       WHERE t.senderAccount.accountNumber = :accountNumber
          OR t.receiverAccount.accountNumber = :accountNumber
       """)
    List<Transaction> findTransactionsByAccountNumber(@Param("accountNumber") String accountNumber);
}
