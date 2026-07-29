package com.example.bankapi.services;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.bankapi.models.Account;
import com.example.bankapi.repos.AccountRepo;
import com.example.bankapi.repos.UserRepo;

@Service
public class AccountService {
    private final AccountRepo accountRepo;
    private final UserRepo userRepo;

    public AccountService(AccountRepo accountRepo, UserRepo userRepo) {
        this.accountRepo = accountRepo;
        this.userRepo = userRepo;
    }
    public List<Account> getAccounts() {
        return accountRepo.findAll();
    }

    public Optional<Account> getAccountById(String id) {
        return accountRepo.findById(id);
    }

    public Account createAccount(Account account) {
        userRepo.findById(account.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));
        return accountRepo.save(account);
    }

    public void deleteAccount(String id) {
        accountRepo.deleteById(id);
    }

    public Account updateAccount(String id, Account updatedAccount) {
        return accountRepo.findById(id).map(existingAccount -> {
                    if (updatedAccount.getUserId() != null) {
                        existingAccount.setUserId(updatedAccount.getUserId());
                    }
                    if (updatedAccount.getBalance() != 0.0) {
                        existingAccount.setBalance(updatedAccount.getBalance());
                    }
                    if (updatedAccount.getAccountType() != null) {
                        existingAccount.setAccountType(updatedAccount.getAccountType());
                    }
                    return accountRepo.save(existingAccount);
                }).orElseThrow(() -> new RuntimeException("Account not found"));
    }
}

