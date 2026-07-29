package com.example.bankapi.controllers;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bankapi.models.Account;
import com.example.bankapi.services.AccountService;


@RestController
@RequestMapping("/api/v1")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts(){
        return ResponseEntity.ok(this.accountService.getAccounts());
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable String id) {
        return this.accountService.getAccountById(id)
        .map(ResponseEntity::ok)
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        return accountService.getAccountById(id)
                .map(a -> {
                    accountService.deleteAccount(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable String id, @RequestBody Account updatedAccount) {
        return accountService.getAccountById(id)
                .map(a -> {
                    return ResponseEntity.ok(accountService.updateAccount(String.valueOf(id), updatedAccount));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
