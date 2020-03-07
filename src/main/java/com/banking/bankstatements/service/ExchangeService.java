package com.banking.bankstatements.service;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class ExchangeService{
    public double exchangeCurrency(double amount, String fromWhatCurrency) throws IOException, JSONException {
        URL url = new URL("https://api.exchangeratesapi.io/latest?symbols="+fromWhatCurrency);
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
        return amount/Double.parseDouble(json.getJSONObject("rates").getString(fromWhatCurrency));
        }
}

