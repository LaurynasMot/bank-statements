package com.banking.bankstatements;

import com.banking.bankstatements.dao.ListDbDataAccessService;
import com.banking.bankstatements.model.Statement;
import com.banking.bankstatements.service.ExchangeService;
import com.banking.bankstatements.service.StatementService;
import org.json.JSONException;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class ExchangeServiceTests {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static ListDbDataAccessService listData = new ListDbDataAccessService();
    private StatementService statementService = new StatementService(listData);

    private ExchangeService exchangeService = new ExchangeService();

    @BeforeClass
    public static void populateDB(){
        if(listData.exportStatement().size()==0) {
            listData.importStatement(new Statement("LT667044060007944471", LocalDateTime.parse("2020-03-05 11:55", formatter), "TEST LT", "", -30.00, "EUR"));
            listData.importStatement(new Statement("LT667044060007944470", LocalDateTime.parse("2020-03-06 20:13", formatter), "TEST LT", "", -50.50, "GBP"));
            listData.importStatement(new Statement("LT667044060007944471", LocalDateTime.parse("2020-03-07 15:33", formatter), "TEST LT", "", 36.44, "EUR"));
            listData.importStatement(new Statement("LT667044060007944470", LocalDateTime.parse("2020-03-08 20:25", formatter), "TEST LT", "", -1.44, "EUR"));
            listData.importStatement(new Statement("LT667044060007944471", LocalDateTime.parse("2020-03-09 08:45", formatter), "TEST LT", "", 0.01, "EUR"));
        }
    }

    @Test
    public void testGetAmountExchanged() throws IOException, JSONException {
        List<Statement> statementTest= new ArrayList<Statement>();
        statementTest.add(new Statement("LT667044060007944470", LocalDateTime.parse("2020-03-06 20:13",formatter),"TEST LT","",-50.50,"GBP"));
        statementTest.add(new Statement("LT667044060007944470", LocalDateTime.parse("2020-03-08 20:25",formatter),"TEST LT","",-1.44,"EUR"));

        double sum = 0;
        for (Statement st:statementTest) {
            if(st.getCurrency().equals("EUR"))
            {
                sum+=st.getAmount();
            }
            else sum+=exchangeService.exchangeCurrency(st.getAmount(), st.getCurrency());
        }
        assertEquals(statementService.getAmount("LT667044060007944470"),sum,0.0001);
    }
}
