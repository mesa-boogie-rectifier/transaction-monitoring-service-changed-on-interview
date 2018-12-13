package com.jpmprgan.frauddetection.domain.service;

import com.jpmprgan.frauddetection.domain.Status;
import com.jpmprgan.frauddetection.domain.Transaction;
import com.jpmprgan.frauddetection.util.UpdateStatusRequest;

import java.util.List;

/**
 * This transaction monitoring service flags suspicious wire transfers when customers are moving money into or out of their accounts
 */


public interface TransactionService {

    /**
     * Verifies a new transaction and sets the status code.
     *
     * @param transaction a new transaction object to be verified by the service
     * @return transaction status code
     */
    Status submitTransaction(Transaction transaction);


    /**
     * Updates transactions that have current status "HOLD" with a new status code received.
     *
     * @param updateStatusRequest an Object containing two fileds,
     *                            new Status to be set on the given transaction on HOLD and
     *                            the transaction Id in the database
     * @return boolean result: true if update was successful, else returns false
     */
    boolean updateTransactionStatus(UpdateStatusRequest updateStatusRequest);

    /**
     * Returns the List of all transactions in the database
     *
     * @return List of all transaction in the database
     */
    List<Transaction> getAllTransactions();

}
