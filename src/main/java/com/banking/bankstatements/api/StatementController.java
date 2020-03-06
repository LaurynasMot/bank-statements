package com.banking.bankstatements.api;

import com.banking.bankstatements.model.Dates;
import com.banking.bankstatements.model.Statement;
import com.banking.bankstatements.service.StatementService;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
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
