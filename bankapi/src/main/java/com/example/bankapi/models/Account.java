package com.example.bankapi.models;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts")

public class Account {
    @Id
    private String id;
    
    private String userId;
    private double balance;
    private String accountType;

    public Account() {
        super();
    }

    public Account(String id, String userId, String accountType) {
        this.id = id;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = 0.00; 
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
