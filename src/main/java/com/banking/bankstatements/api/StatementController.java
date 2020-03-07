package com.banking.bankstatements.api;

import com.banking.bankstatements.model.Dates;
import com.banking.bankstatements.model.Statement;
import com.banking.bankstatements.service.StatementService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@RestController
public class StatementController {
    private final StatementService statementService;

    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @PostMapping("import-csv")
    public void uploadCSVFile(@RequestParam("file") MultipartFile file) {
        statementService.addStatement(file);
    }

    @PostMapping("export-csv")
    public void getAllStatementsInCsv(HttpServletResponse response, @RequestBody(required = false) Dates dates) throws Exception{
        if(dates == null) statementService.getStatementAsCsv(response);
        else {
            if(dates.getDateFrom() == null) statementService.getStatementAsCsvByDate(response, LocalDateTime.MIN, dates.getDateTo());
            if(dates.getDateTo() == null) statementService.getStatementAsCsvByDate(response, dates.getDateFrom(), LocalDateTime.MAX);
            statementService.getStatementAsCsvByDate(response, dates.getDateFrom(), dates.getDateTo());
        }
    }

    @PostMapping("export")
    public List<Statement> getAllStatements(@RequestBody(required = false) Dates dates){
        if(dates == null) return statementService.getStatement();
        else {
            if(dates.getDateFrom() == null) return statementService.getStatementByDate(LocalDateTime.MIN, dates.getDateTo());
            if(dates.getDateTo() == null) return statementService.getStatementByDate(dates.getDateFrom(), LocalDateTime.MAX);
            return statementService.getStatementByDate(dates.getDateFrom(), dates.getDateTo());
        }
    }

    @PostMapping("balance/{nr}")
    public double getBalanceByNumber(@PathVariable("nr") String accountNumber, @RequestBody(required = false) Dates dates) throws IOException, JSONException {
        if(dates == null) return statementService.getAmount(accountNumber);
        else {
            if(dates.getDateFrom() == null) return statementService.getAmountByDate(accountNumber, LocalDateTime.MIN, dates.getDateTo());
            if(dates.getDateTo() == null) return statementService.getAmountByDate(accountNumber, dates.getDateFrom(), LocalDateTime.MAX);
            return statementService.getAmountByDate(accountNumber, dates.getDateFrom(), dates.getDateTo());
        }
    }
}
