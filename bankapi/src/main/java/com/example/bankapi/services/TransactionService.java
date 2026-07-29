package com.example.bankapi.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bankapi.models.Account;
import com.example.bankapi.models.Transaction;
import com.example.bankapi.repos.TransactionRepo;

@Service
public class TransactionService {
    private final TransactionRepo transactionRepo;
    private final AccountService accountService;

    public TransactionService(TransactionRepo transactionRepo, AccountService accountService) {
        this.transactionRepo = transactionRepo;
        this.accountService = accountService;
    }

    public List<Transaction> getTransactions() {
        return transactionRepo.getTransactions();
    }

    public Optional<Transaction> getTransactionById(int id) {
        return transactionRepo.getTransactionById(id);
    }

    public Transaction createTransaction(Transaction transaction) {
        Account account = accountService
            .getAccountById(transaction.getAccountId())
            .orElseThrow(() -> new RuntimeException("Account not found"));
        double amount = transaction.getAmount();
        String transactionType = transaction.getTransactionType();

        if (transactionType.equalsIgnoreCase("DEPOSIT")) {
            account.setBalance(account.getBalance() + amount);
        } 
        else if (transactionType.equalsIgnoreCase("WITHDRAWAL")) {
            if (account.getBalance() < amount) {
                throw new RuntimeException("Declined");
            }
            account.setBalance(account.getBalance() - amount);
        }
        return transactionRepo.createTransaction(transaction);
    }

    public void deleteTransaction(int id) {
        transactionRepo.deleteTransaction(id);
    }
}
