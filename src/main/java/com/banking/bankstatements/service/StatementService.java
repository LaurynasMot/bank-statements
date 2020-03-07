package com.banking.bankstatements.service;

import com.banking.bankstatements.api.BaseException;
import com.banking.bankstatements.dao.StatementDao;
import com.banking.bankstatements.model.Statement;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service("statementService")
@Slf4j
public class StatementService {
    private final StatementDao statementDao;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private ExchangeService exchangeCurrency = new ExchangeService();

    @Autowired
    public StatementService(@Qualifier("listDB") StatementDao statementDao) {
        this.statementDao = statementDao;
    }

    public void addStatement(MultipartFile file){
        if (file.isEmpty()) {
            log.warn("File is empty!");
        }
        else {
            try  {
                String line;
                InputStream is = file.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                line = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    String[] statementString = line.split(",");
                    for (int i  = 0; i<6; i++){
                        if(i==3) continue;
                        if(statementString[i].isBlank()) throw new BaseException("Wrong input");
                    }
                    Statement st = new Statement(statementString[0], LocalDateTime.parse(statementString[1],formatter),statementString[2],statementString[3],Double.parseDouble(statementString[4]),statementString[5]);
                    statementDao.importStatement(st);
                }
            } catch (Exception | BaseException ex) {
                log.error(ex.getMessage());
            }
        }
    }

    public List<Statement> getStatementByDate(LocalDateTime dateFrom, LocalDateTime dateTo){
        return statementDao.exportStatementByDate(dateFrom, dateTo);
    }

    public List<Statement> getStatement(){
        return statementDao.exportStatement();
    }

    public double getAmount(String accountNumber) throws IOException, JSONException {
        double sum = 0;
        List<Statement> statements = statementDao.getStatementsToCalcAmount(accountNumber);
        for (Statement st: statements) {
            if(st.getCurrency().equals("EUR"))
            {
                sum+=st.getAmount();
            }
            else sum+=exchangeCurrency.exchangeCurrency(st.getAmount(), st.getCurrency());
        }
        return sum;
    }

    public double getAmountByDate(String accountNumber, LocalDateTime dateFrom, LocalDateTime dateTo) throws IOException, JSONException {
        double sum = 0;
        List<Statement> statements = statementDao.getStatementsToCalcAmount(accountNumber, dateFrom, dateTo);
        for (Statement st: statements) {
            if(st.getCurrency().equals("EUR"))
            {
                sum+=st.getAmount();
            }
            else sum+=exchangeCurrency.exchangeCurrency(st.getAmount(), st.getCurrency());
        }
        return sum;
    }


    public void getStatementAsCsv(HttpServletResponse response) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
        String fileName ="Statements-";
        fileName += new SimpleDateFormat("yyyyMMddHHmm'.csv'").format(new Date());
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + fileName + "\"");

        StatefulBeanToCsv<Statement> writer = new StatefulBeanToCsvBuilder<Statement>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(statementDao.exportStatement());
    }

    public void getStatementAsCsvByDate(HttpServletResponse response, LocalDateTime dateFrom, LocalDateTime dateTo) throws CsvDataTypeMismatchException, CsvRequiredFieldEmptyException, IOException {
        String fileName ="Statements-";
        fileName += new SimpleDateFormat("yyyyMMddHHmm'.csv'").format(new Date());
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\"" + fileName + "\"");

        StatefulBeanToCsv<Statement> writer = new StatefulBeanToCsvBuilder<Statement>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                .withOrderedResults(false)
                .build();

        writer.write(statementDao.exportStatementByDate(dateFrom, dateTo));
    }
}
