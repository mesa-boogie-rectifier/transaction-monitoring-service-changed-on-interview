package com.jpmprgan.frauddetection;

import com.jpmprgan.frauddetection.domain.Currency;
import com.jpmprgan.frauddetection.domain.Direction;
import com.jpmprgan.frauddetection.domain.Status;
import com.jpmprgan.frauddetection.domain.Transaction;
import com.jpmprgan.frauddetection.domain.service.TransactionService;
import com.jpmprgan.frauddetection.domain.service.TransactionServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDateTime;

@SpringBootApplication
public class TransactionAnalysisApp {

    public static void main(String[] args) {
        SpringApplication.run(TransactionAnalysisApp.class, args);
    }

}
