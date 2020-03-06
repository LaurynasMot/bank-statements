package com.banking.bankstatements.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Data
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
    private String currency; //Galima bandyti pakeisti i currency

    public Statement(String accountNumber, LocalDateTime operationDate, String beneficiary, String comment, double amount, String currency) {
        this.accountNumber = accountNumber;
        this.operationDate = operationDate;
        this.beneficiary = beneficiary;
        this.comment = comment;
        this.amount = amount;
        this.currency = currency;
    }
}
