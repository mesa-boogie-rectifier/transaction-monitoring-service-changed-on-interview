package com.jpmprgan.frauddetection.domain.service;

import com.jpmprgan.frauddetection.domain.Transaction;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class ConvertToUSDStub extends ConvertiontoUSD {

    @Override
    double convertToUSD(Transaction transaction) {
        return transaction.getQuantity();
    }
}
