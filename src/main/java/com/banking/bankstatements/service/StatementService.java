package com.banking.bankstatements.service;

import com.banking.bankstatements.dao.StatementDao;
import com.banking.bankstatements.model.Statement;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.swing.plaf.nimbus.State;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

    public double getAmount(String accountNumber) throws IOException, JSONException {
        List<Statement> statements = statementDao.calculateAmount(accountNumber);
        return exchangeCurrency(statements);
    }

    public double getAmountByDate(String accountNumber, LocalDateTime dateFrom, LocalDateTime dateTo) throws IOException, JSONException {
        List<Statement> statements = statementDao.calculateAmount(accountNumber, dateFrom, dateTo);
        return exchangeCurrency(statements);
    }

    public double exchangeCurrency(List<Statement> statements) throws IOException, JSONException {
        double sum = 0;
        for (Statement st: statements
        ) {
            if(st.getCurrency().equals("EUR"))
            {
                sum+=st.getAmount();
                continue;
            }
            URL url = new URL("https://api.exchangeratesapi.io/latest?symbols="+st.getCurrency());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            JSONObject json = new JSONObject(content.toString());
            sum += st.getAmount()/Double.parseDouble(json.getJSONObject("rates").getString(st.getCurrency()));
        }
        return sum;
    }
}
