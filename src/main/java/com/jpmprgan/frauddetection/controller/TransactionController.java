package com.jpmprgan.frauddetection.controller;

import com.jpmprgan.frauddetection.domain.Status;
import com.jpmprgan.frauddetection.domain.Transaction;
import com.jpmprgan.frauddetection.domain.service.TransactionService;
import com.jpmprgan.frauddetection.util.UpdateStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * RestController that specifies that URL and methods to be use for this service
 */

@RestController
@RequestMapping("/api")
public class TransactionController {


    @Autowired
    TransactionService transactionService;


    @PostMapping("/transactions")
    public Status submitTransaction(@RequestBody Transaction transaction) {
        return transactionService.submitTransaction(transaction);
    }

    @PutMapping("/transactions")
    public boolean updateTransactionStatus(@RequestBody UpdateStatusRequest updateStatusRequest) {
        return transactionService.updateTransactionStatus(updateStatusRequest);
    }

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return transactionService.getAllTransactions();
    }


}
