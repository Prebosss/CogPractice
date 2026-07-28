package com.example.bankapi.models;

public class Customer {
    private Integer id;
    private String name;
    private double balance;

    public Customer() {
        super();
    }

    public Customer(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.balance = 0.00; 
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
