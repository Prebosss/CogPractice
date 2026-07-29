package com.example.bankapi.repos;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.example.bankapi.models.Account;

//This is the repo for account which will handle all account related actions for a bank account.
@Repository
public class AccountRepo {
    private final List<Account> accounts = new ArrayList<>();
    public AccountRepo() {
        accounts.add(new Account(1, 1, "CHECKING"));
        accounts.add(new Account(2, 2, "SAVINGS"));
        accounts.add(new Account(3, 3, "CHECKING"));
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public Optional<Account> getAccountById(Integer id) {
        List<Account> accounts = getAccounts();
        return accounts.stream().filter(a -> a.getId() == id).findFirst();
    }

    public Account createAccount(Account account) {
        account.setId(accounts.size() + 1);
        accounts.add(account);
        return account;
    }

    public void deleteAccount(int id) {
        accounts.removeIf(a -> a.getId() == id);
    }
    
    public Account updateAccount(int id, Account updatedAccount) {
            Account existingAccount = getAccountById(id).get();
            if (updatedAccount.getAccountType() != null) {
                existingAccount.setAccountType(updatedAccount.getAccountType());
            }
            if (updatedAccount.getBalance() != 0.0) {
                existingAccount.setBalance(existingAccount.getBalance() + updatedAccount.getBalance());
            }
            return existingAccount;
    }
}
