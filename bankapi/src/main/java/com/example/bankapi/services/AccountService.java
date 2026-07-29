package com.example.bankapi.services;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.bankapi.models.Account;
import com.example.bankapi.repos.AccountRepo;

@Service
public class AccountService {
    private final AccountRepo accountRepo;
    public AccountService(AccountRepo accountRepo) {
        this.accountRepo = accountRepo;
    }
    public List<Account> getAccounts() {
        return accountRepo.getAccounts();
    }

    public Optional<Account> getAccountById(int id) {
        return accountRepo.getAccountById(id);
    }

    public Account createAccount(Account account) {
        return accountRepo.createAccount(account);
    }

    public void deleteAccount(int id) {
        accountRepo.deleteAccount(id);
    }

    public Account updateAccount(int id, Account updatedAccount) {
        return accountRepo.updateAccount(id, updatedAccount);
    }
}

