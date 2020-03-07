package com.banking.bankstatements.dao;

import com.banking.bankstatements.entity.Statement;

import java.time.LocalDateTime;
import java.util.List;

public interface StatementDao {

    int importStatement(Statement statement);

    List<Statement> exportStatementByDate(LocalDateTime dateFrom, LocalDateTime dateTo);

    List<Statement> exportStatement();

    List<Statement> findStatementsToCalcAmount(String accountNumber);

    List<Statement> findStatementsToCalcAmount(String accountNumber, LocalDateTime dateFrom, LocalDateTime dateTo);

}
