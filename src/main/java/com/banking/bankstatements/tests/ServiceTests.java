package com.banking.bankstatements.tests;


import com.banking.bankstatements.dao.ListDbDataAccessService;
import com.banking.bankstatements.model.Statement;
import com.banking.bankstatements.service.ExchangeService;
import com.banking.bankstatements.service.StatementService;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class ServiceTests {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    static ListDbDataAccessService listData = new ListDbDataAccessService();
    StatementService statementService = new StatementService(listData);
    ExchangeService exchangeService = new ExchangeService("EUR");

    @BeforeClass
    public static void populateDB(){
        listData.importStatement(new Statement("LT667044060007944471", LocalDateTime.parse("2020-03-05 11:55",formatter),"TEST LT","",-30.00,"EUR"));
        listData.importStatement(new Statement("LT667044060007944470", LocalDateTime.parse("2020-03-06 20:13",formatter),"TEST LT","",-50.50,"GBP"));
        listData.importStatement(new Statement("LT667044060007944471", LocalDateTime.parse("2020-03-07 15:33",formatter),"TEST LT","",36.44,"EUR"));
        listData.importStatement(new Statement("LT667044060007944470", LocalDateTime.parse("2020-03-08 20:25",formatter),"TEST LT","",-1.44,"EUR"));
        listData.importStatement(new Statement("LT667044060007944471", LocalDateTime.parse("2020-03-09 08:45",formatter),"TEST LT","",0.01,"EUR"));
    }

    @Test
    public void testGetAmount() throws IOException, JSONException {
        assertEquals(statementService.getAmount("LT667044060007944471"),6.45,0.0001);
    }

    @Test
    public void testGetAmountByDate() throws IOException, JSONException {
        assertEquals(statementService.getAmountByDate("LT667044060007944471", LocalDateTime.parse("2020-03-05 10:10",formatter), LocalDateTime.parse("2020-03-08 20:57",formatter)),6.44,0.0001);
    }

    @Test
    public void testGetAmountExchanged() throws IOException, JSONException {
        List<Statement> statementTest= new ArrayList<Statement>();
        statementTest.add(new Statement("LT667044060007944470", LocalDateTime.parse("2020-03-06 20:13",formatter),"TEST LT","",-50.50,"GBP"));
        statementTest.add(new Statement("LT667044060007944470", LocalDateTime.parse("2020-03-08 20:25",formatter),"TEST LT","",-1.44,"EUR"));

        assertEquals(statementService.getAmount("LT667044060007944470"),exchangeService.exchangeCurrency(statementTest),0.0001);
    }
}