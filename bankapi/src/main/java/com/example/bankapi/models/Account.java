package com.example.bankapi.models;

public class Account {
    private Integer id;
    private Integer userId;
    private double balance;
    private String accountType;

    public Account() {
        super();
    }

    public Account(Integer id, Integer userId, String accountType) {
        this.id = id;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = 0.00; 
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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
