package com.banking.bankstatements.dao;

import com.banking.bankstatements.model.Statement;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("listDB")
public class ListDbDataAccessService implements StatementDao {
    private static List<Statement> DB = new ArrayList<>();

    @Override
    public int importStatement(Statement statement) {
        DB.add(statement);
        return 1;
    }

    @Override
    public List<Statement> exportStatementByDate(LocalDateTime dateFrom, LocalDateTime dateTo) {
        System.out.println("test");
        System.out.println(dateFrom);
        System.out.println(dateTo);
        System.out.println(DB.stream()
                .filter(dates -> dates.getOperationDate().isAfter(dateFrom) && dates.getOperationDate().isBefore(dateTo)).collect(Collectors.toList()).toString());
        return DB.stream()
                .filter(dates -> dates.getOperationDate().isAfter(dateFrom) && dates.getOperationDate().isBefore(dateTo)).collect(Collectors.toList());
    }

    @Override
    public List<Statement> exportStatement() {
        return DB;
    }

    @Override
    public double calculateAmount(String accountNumber) {
        return DB.stream().filter(db -> db.getAccountNumber().equals(accountNumber)).mapToDouble(Statement::getAmount).sum();
    }

    @Override
    public double calculateAmount(String accountNumber, LocalDateTime dateFrom, LocalDateTime dateTo) {
        return DB.stream().filter(db -> db.getAccountNumber().equals(accountNumber) && db.getOperationDate().isAfter(dateFrom) && db.getOperationDate().isBefore(dateTo)).mapToDouble(Statement::getAmount).sum();
    }
}
