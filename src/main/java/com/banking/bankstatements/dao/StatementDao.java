package com.banking.bankstatements.dao;

import com.banking.bankstatements.model.Statement;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StatementDao {

    int importStatement(Statement statement);

    List<Statement> exportStatementByDate(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Statement> exportStatement();

    double calculateAmount(String accountNumber);

    double calculateAmount(String accountNumber, LocalDateTime dateFrom, LocalDateTime dateTo);
}
