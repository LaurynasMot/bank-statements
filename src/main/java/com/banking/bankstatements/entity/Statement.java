package com.banking.bankstatements.entity;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class Statement {
    @CsvBindByName @Getter @Setter
    private String accountNumber;
    @CsvBindByName @Getter @Setter
    private LocalDateTime operationDate;
    @CsvBindByName @Getter @Setter
    private String beneficiary;
    @CsvBindByName @Getter @Setter
    private String comment;
    @CsvBindByName @Getter @Setter
    private double amount;
    @CsvBindByName @Getter @Setter
    private String currency;

    public Statement(String accountNumber, LocalDateTime operationDate, String beneficiary, String comment, double amount, String currency) {
        this.accountNumber = accountNumber;
        this.operationDate = operationDate;
        this.beneficiary = beneficiary;
        this.comment = comment;
        this.amount = amount;
        this.currency = currency;
    }
}
