package com.banking.bankstatements.api;

import com.banking.bankstatements.request.Dates;
import com.banking.bankstatements.entity.Statement;
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
@RequestMapping("/statements")
public class StatementController {
    private final StatementService statementService;

    public StatementController(StatementService statementService) {
        this.statementService = statementService;
    }

    @PostMapping
    public void uploadCSVFile(@RequestParam("file") MultipartFile file) {
        statementService.addStatement(file);
    }

    @GetMapping
    public void getAllStatementsInCsv(HttpServletResponse response, @RequestBody(required = false) Dates dates) throws Exception{
        statementService.getStatementAsCsv(response, dates);
    }

    @GetMapping("json")
    public List<Statement> getAllStatements(@RequestBody(required = false) Dates dates){
       return statementService.getStatement(dates);
    }

    @GetMapping("{nr}")
    public double getBalanceByNumber(@PathVariable("nr") String accountNumber, @RequestBody(required = false) Dates dates) throws IOException, JSONException {
        if(dates == null) return statementService.getAmount(accountNumber);
        else {
            if(dates.getDateFrom() == null) return statementService.getAmountByDate(accountNumber, LocalDateTime.MIN, dates.getDateTo());
            if(dates.getDateTo() == null) return statementService.getAmountByDate(accountNumber, dates.getDateFrom(), LocalDateTime.MAX);
            return statementService.getAmountByDate(accountNumber, dates.getDateFrom(), dates.getDateTo());
        }
    }
}
