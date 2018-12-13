package com.jpmprgan.frauddetection.domain.service;

import com.jpmprgan.frauddetection.domain.Currency;
import com.jpmprgan.frauddetection.domain.Direction;
import com.jpmprgan.frauddetection.domain.Status;
import com.jpmprgan.frauddetection.domain.Transaction;
import com.jpmprgan.frauddetection.util.UpdateStatusRequest;
import com.sun.media.sound.DirectAudioDeviceProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionServiceImplTest {

    private TransactionService transactionService = new TransactionServiceImpl(new ConvertToUSDStub());

    /**
     * Populates the database with the new transactions before each test
     */

    @BeforeEach
    void setUp() {

        System.out.println("Populating the list of random transactions \n");

        for (int i = 0; i < 10; i++) {
            Transaction transaction = new Transaction(LocalDateTime.now(),
                    (long) i, (long) (100 + i),
                    Direction.values()[new Random().nextInt(2)],
                    Currency.USD, 1000.0 + i,
                    Status.values()[new Random().nextInt(4)]);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            transactionService.submitTransaction(transaction);
        }
        // data for test cases
        Transaction transaction = new Transaction(LocalDateTime.now(),
                1L, (100000L), Direction.DEBIT,
                Currency.USD, 1_000_000.0, null);
        Transaction transaction1 = new Transaction(LocalDateTime.now(),
                1L, (100000L), Direction.DEBIT,
                Currency.USD, 1_000_000.0, null);

        Transaction transaction2 = new Transaction(LocalDateTime.now(),
                2L, (200L), Direction.DEBIT,
                Currency.RUB, 1_000_000.0, null);
        transactionService.submitTransaction(transaction);
        transactionService.submitTransaction(transaction1);
        transactionService.submitTransaction(transaction2);


//        System.out.println("Printing all transactions\n");
//        for (Transaction tr : transactionService.getAllTransactions()) {
//            System.out.println(tr);
//        }
    }

    /**
     * Method: Submit transaction
     * if Transaction = null
     *
     * @result if transaction equals to null, the method return <code>null</code>
     */

    @Test
    void submitTransactionIfNullTest() {
        assertNull(transactionService.submitTransaction(null));
//        assertThrows(RuntimeException.class, () -> transactionService.submitTransaction(null));
    }

    /**
     * Method: Submit transaction
     * if Direction = CREDIT
     *
     * @result if Direction is CREDIT, the method immediately returns <code>ACCEPT</code>
     */
    @Test
    void submitTransactionIfCreditTest() {

        System.out.println("Testing new transaction with direction = Credit");
        Transaction transaction = new Transaction(LocalDateTime.now(), 1L, (1000L), Direction.CREDIT, Currency.USD, 10000.0, null);


        Status status = transactionService.submitTransaction(transaction);
        assertEquals(Status.ACCEPT, status);
        System.out.println("Transaction status " + status);
    }

    /**
     * Method: Submit transaction
     * if Similar DEBIT transactions with counterpart account exists
     *
     * @result returns status <code>HOLD</code>
     */
    @Test
    void submitTransactionIfRejectedOrHoldTest() {

        System.out.println("Testing DEBIT transaction and comparing to the rejectedOrHold transactins");
        Transaction transaction = new Transaction(LocalDateTime.now(), 1L, (100000L), Direction.DEBIT, Currency.USD, 10000.0, null);



        Status status = transactionService.submitTransaction(transaction);
        assertEquals(Status.HOLD, status);
        System.out.println("Transaction status " + status);
    }

    /**
     * Method: Submit transaction
     * if quantity in USD is less than limit
     *
     * @result returns status <code>ACCEPT</code>
     */

    @Test
    void submitTransactionIfLessThanLimitTest() {




        System.out.println("Testing DEBIT transaction and comparing to unmonitored amount");
        Transaction transaction = new Transaction(LocalDateTime.now(), 1L, (100001L), Direction.DEBIT, Currency.USD, 500.0, null);

        Status status = transactionService.submitTransaction(transaction);
        assertEquals(Status.ACCEPT, status);
        System.out.println("Transaction status " + status);

        System.out.println();

        System.out.println("Testing DEBIT transaction and comparing to unmonitored amount");
        Transaction transaction1 = new Transaction(LocalDateTime.now(), 1L, (100001L), Direction.DEBIT, Currency.ILS, 3300.0, null);

        Status status1 = transactionService.submitTransaction(transaction1);
        assertEquals(Status.ACCEPT, status1);
        System.out.println("Transaction status " + status1);


    }

    /**
     * Method: Submit transaction
     * if more than a limit and but have  similar DEBIT  transactions with between same
     * Customer Account and Counterpart Account with status <code>APPROVED</code>
     *
     * @result returns status <code>ACCEPT</code>
     */

    @Test
    void submitTransactionIfPreviouslyApprovedTest() {


        System.out.println("Generating test data for case ifPreviouslyApproved");
        Transaction testTransaction = new Transaction(LocalDateTime.now(),
                2123L, (long) (20023L), Direction.DEBIT,
                Currency.RUB, 1_000_000.0, null);
        transactionService.submitTransaction(testTransaction);
        transactionService.updateTransactionStatus(new UpdateStatusRequest(Status.APPROVED, testTransaction.getTransactionId()));
        Transaction transaction = new Transaction(LocalDateTime.now(), 2123L, (long) (20023L), Direction.DEBIT, Currency.USD, 2_000_000.0, null);
        System.out.println("Testing new transaction");
        Status status = transactionService.submitTransaction(transaction);
        assertEquals(Status.ACCEPT, status);
        System.out.println("Transaction status " + status);
    }

    /**
     * Method: getAllTransactions()
     * get list of all transactions
     */
    @Test
    void getAllTransactionsTest() throws Exception {
        System.out.println("Printing list of all transactions");
        List<Transaction> allTransactions = transactionService.getAllTransactions();
        assertNotNull(allTransactions);
    }

    /**
     * Metod: updateTransactionStatus()
     * check whether status of the transactions on HOLD was successfully set based on input data
     * returns true if status has been set
     */

    @Test
    void updateTransactionStatusIfTrueTest() {
        Transaction transaction = new Transaction(LocalDateTime.now(),
                1123L, (100000L), Direction.DEBIT,
                Currency.USD, 1_000_000.0, null);
        transactionService.submitTransaction(transaction);

        UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest();
        updateStatusRequest.setStatus(Status.APPROVED);
        updateStatusRequest.setTransactionId(transaction.getTransactionId());

        boolean result = transactionService.updateTransactionStatus(updateStatusRequest);

        assertTrue(result);
        assertEquals(Status.APPROVED, transaction.getStatus());

    }

    /**
     * Metod: updateTransactionStatus()
     * check whether status of the transactions on HOLD was successfully set based on input data
     * returns true if status has been set
     */

    @Test
    void updateTransactionStatusIfFalseTest() {
        Transaction transaction = new Transaction(LocalDateTime.now(),
                1123L, (100000L), Direction.DEBIT,
                Currency.USD, 1_000_000.0, null);
        transactionService.submitTransaction(transaction);

        UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest();
        updateStatusRequest.setStatus(Status.REJECTED);
        updateStatusRequest.setTransactionId(transaction.getTransactionId());

        boolean result = transactionService.updateTransactionStatus(updateStatusRequest);

        assertTrue(result);
        assertEquals(Status.REJECTED, transaction.getStatus());

    }

    /**
     * Metod: updateTransactionStatus()
     * check whether status of the transactions on HOLD was successfully set based on input data
     * returns true if status has been set
     * returns false if transaction in database has statuses other then "HOLD"
     */

    @Test
    void updateTransactionStatusIfOther() {
        Transaction transaction = new Transaction(LocalDateTime.now(),
                1123L, (100000L), Direction.CREDIT,
                Currency.USD, 1.0, null);
        transactionService.submitTransaction(transaction);

        UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest();
        updateStatusRequest.setStatus(Status.REJECTED);
        updateStatusRequest.setTransactionId(transaction.getTransactionId());

        boolean result = transactionService.updateTransactionStatus(updateStatusRequest);

        assertFalse(result);

    }

    /**
     * Metod: updateTransactionStatus()
     * @return false in there is not transation with such id in the database
     */
    @Test
    void updateTransactionStatusTransactionDoesntExists() {

        Transaction transaction = new Transaction(LocalDateTime.now(),
                1123L, (100000L), Direction.CREDIT,
                Currency.USD, 1.0, null);
        UpdateStatusRequest updateStatusRequest = new UpdateStatusRequest();
        updateStatusRequest.setStatus(Status.APPROVED);
        updateStatusRequest.setTransactionId(transaction.getTransactionId());
        boolean result = transactionService.updateTransactionStatus(updateStatusRequest);
        assertFalse(result);
//        assertThrows(RuntimeException.class, () -> transactionService.updateTransactionStatus(null));

    }

    /**
     * Metod: updateTransactionStatus()
     * If method invoiked with null object throws Runtime exception
     */
    @Test
    void updateTransactionStatusNull() {

        boolean result = transactionService.updateTransactionStatus(null);
        assertFalse(result);

    }


    @Test
    void compareTransactionIdsTest() {
        Transaction transaction1 = new Transaction(LocalDateTime.now(), 1234L, 231L, Direction.CREDIT, Currency.USD, 1000, null);
        Transaction transaction2 = new Transaction(LocalDateTime.now(), 1234L, 231L, Direction.CREDIT, Currency.USD, 1000, null);

        assertFalse(transaction1.getTransactionId()== transaction2.getTransactionId());

    }
}