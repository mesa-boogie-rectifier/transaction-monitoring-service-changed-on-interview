package com.jpmprgan.frauddetection.domain.service;

import com.jpmprgan.frauddetection.domain.Currency;
import com.jpmprgan.frauddetection.domain.Direction;
import com.jpmprgan.frauddetection.domain.Status;
import com.jpmprgan.frauddetection.domain.Transaction;
import com.jpmprgan.frauddetection.util.CurrencyDate;
import com.jpmprgan.frauddetection.util.UpdateStatusRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Implementation of the transaction monitoring service API
 */

@Service
public class TransactionServiceImpl implements TransactionService {


    private static final Logger LOGGER = Logger.getLogger(TransactionServiceImpl.class.getName());

    /**
     * The amount of <>UNMONITORED_QUANTITY</>
     */
    private static final double UNMONITORED_QUANTITY = 1000.0;

    /**
     * Simplified data structure to store all incoming transactions
     * uses Transaction id as a key, and Transaction object as a value
     */
    private Map<String, Transaction> transactions = new HashMap<>();

    /**
     * Simple implementation of the database to store transactions with status "HOLD" or "REJECTED"
     * uses CounterpartAccountNumber as a key and Transaction object as a value
     */
    private Map<Long, Transaction> rejectedTransactions = new HashMap<>();
    /**
     * Simple implementation of the database to store transactions with status "APPROVED"
     * uses CouterpartAccountNumber & CustomerAccountNumber as a compound key and Transaction object as a value
     */
    private Map<String, Transaction> approvedTransactions = new HashMap<>();

    @Autowired
    private ConvertiontoUSD convertiontoUSD;

    public TransactionServiceImpl(ConvertiontoUSD convertiontoUSD) {
        this.convertiontoUSD = convertiontoUSD;
    }

    @Override
    public Status submitTransaction(Transaction transaction) {

        if (transaction == null) {
            LOGGER.warning("Transaction is NULL, please provide non NULL transaction object");
            return null;
        }

        if (transaction.getDirection() == Direction.CREDIT) {
            transaction.setStatus(Status.ACCEPT);
            transactions.putIfAbsent(transaction.getTransactionId(), transaction);
            LOGGER.info("CREDIT Transaction with id=" + transaction.getTransactionId() + " got status " + transaction.getStatus());
            return Status.ACCEPT;
        }

        Long counterpartKey = transaction.getCounterpartAccountNumber();
        if (rejectedTransactions.containsKey(counterpartKey)) {
            transaction.setStatus(Status.HOLD);
            transactions.putIfAbsent(transaction.getTransactionId(), transaction);
            rejectedTransactions.putIfAbsent(counterpartKey, transaction);
            LOGGER.info("Exists a number of similar DEBIT transactions on HOLD. Waiting for status change for transactionId " + transaction.getTransactionId());
            return Status.HOLD;
        } else {

            LOGGER.info("Calculating the USD amount of the transaction ");



            double amountInUSD = convertiontoUSD.convertToUSD(transaction);

            if (amountInUSD < UNMONITORED_QUANTITY) {
                transaction.setStatus(Status.ACCEPT);
                transactions.putIfAbsent(transaction.getTransactionId(), transaction);
                LOGGER.info("DEBIT Transaction with id=" + transaction.getTransactionId() + " got status " + transaction.getStatus());
                return Status.ACCEPT;
            } else {
                String compoundKey = String.valueOf(transaction.getCustomerAccountNumber())
                        + String.valueOf(transaction.getCounterpartAccountNumber());
                if (approvedTransactions.containsKey(compoundKey)) {
                    transaction.setStatus(Status.ACCEPT);
                    transactions.putIfAbsent(transaction.getTransactionId(), transaction);
                    LOGGER.info("A similar DEBIT transaction was already Approved by the investigator " + transaction.getTransactionId() + " setting status to " + transaction.getStatus());
                    return Status.ACCEPT;
                }
                transaction.setStatus(Status.HOLD);
                transactions.putIfAbsent(transaction.getTransactionId(), transaction);
                rejectedTransactions.putIfAbsent(counterpartKey, transaction);
                LOGGER.info("DEBIT Transaction with id=" + transaction.getTransactionId() + " need additional investigation");
                return Status.HOLD;
            }
        }
    }

    @Override
    public boolean updateTransactionStatus(UpdateStatusRequest updateStatusRequest) {
        if (updateStatusRequest == null) {
            LOGGER.warning("Request for Update equals to NULL");
            return false;
        }
        Transaction transaction = transactions.get(updateStatusRequest.getTransactionId());

        if (transaction == null) {
            LOGGER.warning("no transaction found for update");
            return false;
        }
        if (transaction.getStatus() == Status.HOLD) {

            if (updateStatusRequest.getStatus() == Status.APPROVED) {
                transaction.setStatus(updateStatusRequest.getStatus());
                rejectedTransactions.remove(transaction.getCounterpartAccountNumber());
                String compoundKey = String.valueOf(transaction.getCustomerAccountNumber())
                        + String.valueOf(transaction.getCounterpartAccountNumber());
                approvedTransactions.put(compoundKey, transaction);
                LOGGER.info("Transaction with id " + transaction.getTransactionId() + " was approved by the investigator for further processing");
                return true;
            } else if (updateStatusRequest.getStatus() == Status.REJECTED) {
                transaction.setStatus(updateStatusRequest.getStatus());
                LOGGER.info("Transaction with id " + transaction.getTransactionId() + " was rejected by the investigator for further processing");
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        LOGGER.info("Printing all transactions");
        for (Transaction tr : transactions.values()) {
            System.out.println(tr);
        }
        return new ArrayList<>(transactions.values());
    }


    /**
     * Recalculates the quantity of the given transaction in US dollars
     *
     * @param transaction a given transaction which quantity should be converted in USD and recalculated
     * @return amount of the transaction  in US dollars with type "double"
     */

}
