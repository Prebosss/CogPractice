package com.example.bankapi.models;

public class Transaction {
    private Integer id;
    private Integer accountId;
    private double amount;
    private String transactionType;

    public Transaction() {
        super();
    }

    public Transaction(Integer id, Integer accountId, double amount, String transactionType) {
        this.id = id;
        this.accountId = accountId;
        this.transactionType = transactionType;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    //WITHDRAWAL, DEPOSIT
    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
