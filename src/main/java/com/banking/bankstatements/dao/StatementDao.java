package com.banking.bankstatements.dao;

import com.banking.bankstatements.model.Statement;

import java.time.LocalDateTime;
import java.util.List;

public interface StatementDao {

    int importStatement(Statement statement);

    List<Statement> exportStatementByDate(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Statement> exportStatement();

    List<Statement> getStatementsToCalcAmount(String accountNumber);

    List<Statement> getStatementsToCalcAmount(String accountNumber, LocalDateTime dateFrom, LocalDateTime dateTo);

}
