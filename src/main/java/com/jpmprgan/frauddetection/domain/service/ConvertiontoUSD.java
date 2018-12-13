package com.jpmprgan.frauddetection.domain.service;

import com.jpmprgan.frauddetection.domain.Currency;
import com.jpmprgan.frauddetection.domain.Transaction;
import com.jpmprgan.frauddetection.util.CurrencyDate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class ConvertiontoUSD {


    double convertToUSD(Transaction transaction) {
        if (transaction.getCurrency() == Currency.USD)
            return transaction.getQuantity();

        RestTemplate rest = new RestTemplate();
        String url = "http://data.fixer.io/api/latest?access_key=81ebf276e1ed808b58591b5fb05c34eb";
        ResponseEntity<CurrencyDate> response = rest.exchange(url, HttpMethod.GET, null, CurrencyDate.class);
        Map<String, Double> rates = response.getBody().getRates();

        return transaction.getQuantity() / rates.get(transaction.getCurrency().toString())
                * rates.get(Currency.USD.toString());
    }
}
