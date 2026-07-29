package com.example.bankapi.repos;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.bankapi.models.Transaction;

//This is the repo for transactions which handles all actions for related bank account
@Repository
public class TransactionRepo {
    private final List<Transaction> transactions = new ArrayList<>();
    public TransactionRepo() {
        transactions.add(new Transaction(1, 1, 100.00, "DEPOSIT"));
        transactions.add(new Transaction(2, 1, 50.00, "WITHDRAWAL"));
        transactions.add(new Transaction(3, 2, 200.00, "DEPOSIT"));
        transactions.add(new Transaction(4, 3, 75.00, "WITHDRAWAL"));
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Optional<Transaction> getTransactionById(Integer id) {
        return this.transactions.stream().filter(t -> t.getId() == id).findFirst();
    }

    public Transaction createTransaction(Transaction transaction) {
        transaction.setId(transactions.size() + 1);
        transactions.add(transaction);
        return transaction;
    }

    public void deleteTransaction(int id) {
        transactions.removeIf(t -> t.getId() == id);
    }
}
