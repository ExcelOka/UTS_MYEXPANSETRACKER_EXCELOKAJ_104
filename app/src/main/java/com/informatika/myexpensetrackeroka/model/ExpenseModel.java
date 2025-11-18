package com.informatika.myexpensetrackeroka.model;

import java.io.Serializable;

public class ExpenseModel implements Serializable {
    private String transactionName;
    private double amount;
    private String category;
    private String date;

    public ExpenseModel(String transactionName, double amount, String category, String date) {
        this.transactionName = transactionName;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getTransactionName() { return transactionName; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getDate() { return date; }
}
