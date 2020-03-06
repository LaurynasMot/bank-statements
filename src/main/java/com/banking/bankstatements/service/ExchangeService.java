package com.banking.bankstatements.service;

import com.banking.bankstatements.model.Statement;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class ExchangeService {
    @Getter @Setter
    private String toWhatCurrency;

    public ExchangeService(String toWhatCurrency) {
        this.toWhatCurrency = toWhatCurrency;
    }

    public double exchangeCurrency(List<Statement> statements) throws IOException, JSONException {
        double sum = 0;
        for (Statement st: statements
        ) {
            if(st.getCurrency().equals(toWhatCurrency))
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
