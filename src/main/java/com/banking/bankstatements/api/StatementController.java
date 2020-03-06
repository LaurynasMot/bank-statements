package com.banking.bankstatements.api;

import com.banking.bankstatements.model.Statement;
import com.banking.bankstatements.service.StatementService;
import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

@RestController
public class StatementController {
    private final StatementService statementService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @PostMapping("/import-csv")
    public void uploadCSVFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            System.out.println("File is empty");
        }
        else {
            try  {
                String line;
                InputStream is = file.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] statementString = line.split(",");
                    Statement st = new Statement(statementString[0], LocalDateTime.parse(statementString[1],formatter),statementString[2],statementString[3],Double.parseDouble(statementString[4]),statementString[5]);
                    statementService.addStatement(st);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @GetMapping
    public List<Statement> getAllStatements(){
        return statementService.getStatement();
    }

    @GetMapping(value = "{timeFrom}-{timeTo}")
    public List<Statement> getAllStatementsByDate(@PathVariable("timeFrom") long dateFrom, @PathVariable("timeTo") long dateTo){
        return statementService.getStatementByDate(Instant.ofEpochSecond(dateFrom).atZone(ZoneId.systemDefault()).toLocalDateTime(), Instant.ofEpochSecond(dateTo).atZone(ZoneId.systemDefault()).toLocalDateTime());
    }

    @GetMapping(value = "balance/{nr}")
    public double getBalanceByNumber(@PathVariable("nr") String accountNumber){
        return statementService.getAmount(accountNumber);
    }

    @GetMapping(value = "balance/{nr}/{timeFrom}-{timeTo}")
    public double getBalanceByNumberAndDate(@PathVariable("nr") String accountNumber, @PathVariable("timeFrom") long dateFrom, @PathVariable("timeTo") long dateTo){
        return statementService.getAmountByDate(accountNumber,
                Instant.ofEpochSecond(dateFrom).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                Instant.ofEpochSecond(dateTo).atZone(ZoneId.systemDefault()).toLocalDateTime());
    }
}
