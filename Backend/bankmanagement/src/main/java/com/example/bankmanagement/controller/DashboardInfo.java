// DashboardInfo.java

package com.example.bankmanagement.controller;

public class DashboardInfo {
    private String username;
    private String accountName;
    private double balance;

    public DashboardInfo(String username, String accountName, double balance) {
        this.username = username;
        this.accountName = accountName;
        this.balance = balance;
    }

    // Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
