package com.banking.bankstatements.service;

import com.banking.bankstatements.dao.StatementDao;
import com.banking.bankstatements.model.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.swing.plaf.nimbus.State;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class StatementService {
    private final StatementDao statementDao;

    @Autowired
    public StatementService(@Qualifier("listDB") StatementDao statementDao) {
        this.statementDao = statementDao;
    }

    public int addStatement(Statement statement){
        return statementDao.importStatement(statement);
    }

    public List<Statement> getStatementByDate(LocalDateTime dateFrom, LocalDateTime dateTo){
        return statementDao.exportStatementByDate(dateFrom, dateTo);
    }

    public List<Statement> getStatement(){
        return statementDao.exportStatement();
    }

    public double getAmount(String accountNumber){
        return statementDao.calculateAmount(accountNumber);
    }

    public double getAmountByDate(String accountNumber, LocalDateTime dateFrom, LocalDateTime dateTo){
        return statementDao.calculateAmount(accountNumber, dateFrom, dateTo);
    }
}
