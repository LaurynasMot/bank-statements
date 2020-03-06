package com.banking.bankstatements.api;

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

    @PostMapping("/import-csv")
    public void uploadCSVFile(@RequestParam("file") MultipartFile file) {
        statementService.addStatement(file);
    }

    @GetMapping("export-csv")
    public void getAllStatementsInCsv(HttpServletResponse response) throws Exception{
        statementService.getStatementAsCsv(response);
    }

    @GetMapping("export")
    public List<Statement> getAllStatements(){
        return statementService.getStatement();
    }

    @GetMapping(value = "{timeFrom}-{timeTo}")
    public List<Statement> getAllStatementsByDate(@PathVariable("timeFrom") long dateFrom, @PathVariable("timeTo") long dateTo){
        return statementService.getStatementByDate(Instant.ofEpochSecond(dateFrom).atZone(ZoneId.systemDefault()).toLocalDateTime(), Instant.ofEpochSecond(dateTo).atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    @GetMapping(value = "balance/{nr}")
    public double getBalanceByNumber(@PathVariable("nr") String accountNumber) throws IOException, JSONException {
        return statementService.getAmount(accountNumber);
    }

    @GetMapping(value = "balance/{nr}/{timeFrom}-{timeTo}")
    public double getBalanceByNumberAndDate(@PathVariable("nr") String accountNumber, @PathVariable("timeFrom") long dateFrom, @PathVariable("timeTo") long dateTo) throws IOException, JSONException {
        return statementService.getAmountByDate(accountNumber,
                Instant.ofEpochSecond(dateFrom).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                Instant.ofEpochSecond(dateTo).atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
}
