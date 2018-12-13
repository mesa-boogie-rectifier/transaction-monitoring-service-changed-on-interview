package com.jpmprgan.frauddetection.domain;

import java.time.LocalDateTime;
import java.util.Random;

/**
 * A model of the transaction to be verified by the transaction monitoring service
 */
public class Transaction {

    private String transactionId;
    private LocalDateTime dateTime;
    private long customerAccountNumber;
    private long counterpartAccountNumber;
    private Direction direction;
    private Currency currency;
    private double quantity;
    private Status status;

    public Transaction() {
    }

    public Transaction(LocalDateTime dateTime, long customerAccountNumber, long counterpartAccountNumber, Direction direction, Currency currency, double quantity, Status status) {

        this.transactionId = String.valueOf(System.currentTimeMillis()) + String.valueOf((char)(new Random().nextInt(26) + 'a')).toUpperCase();
        this.dateTime = dateTime;
        this.customerAccountNumber = customerAccountNumber;
        this.counterpartAccountNumber = counterpartAccountNumber;
        this.direction = direction;
        this.currency = currency;
        this.quantity = quantity;
        this.status = status;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public long getCustomerAccountNumber() {
        return customerAccountNumber;
    }

    public void setCustomerAccountNumber(long customerAccountNumber) {
        this.customerAccountNumber = customerAccountNumber;
    }

    public long getCounterpartAccountNumber() {
        return counterpartAccountNumber;
    }

    public void setCounterpartAccountNumber(long counterpartAccountNumber) {
        this.counterpartAccountNumber = counterpartAccountNumber;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction)) return false;

        Transaction that = (Transaction) o;

        if (getCustomerAccountNumber() != that.getCustomerAccountNumber()) return false;
        if (getCounterpartAccountNumber() != that.getCounterpartAccountNumber()) return false;
        if (Double.compare(that.getQuantity(), getQuantity()) != 0) return false;
        if (getTransactionId() != null ? !getTransactionId().equals(that.getTransactionId()) : that.getTransactionId() != null)
            return false;
        if (getDateTime() != null ? !getDateTime().equals(that.getDateTime()) : that.getDateTime() != null)
            return false;
        if (getDirection() != that.getDirection()) return false;
        if (getCurrency() != that.getCurrency()) return false;
        return getStatus() == that.getStatus();
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getTransactionId() != null ? getTransactionId().hashCode() : 0;
        result = 31 * result + (getDateTime() != null ? getDateTime().hashCode() : 0);
        result = 31 * result + (int) (getCustomerAccountNumber() ^ (getCustomerAccountNumber() >>> 32));
        result = 31 * result + (int) (getCounterpartAccountNumber() ^ (getCounterpartAccountNumber() >>> 32));
        result = 31 * result + (getDirection() != null ? getDirection().hashCode() : 0);
        result = 31 * result + (getCurrency() != null ? getCurrency().hashCode() : 0);
        temp = Double.doubleToLongBits(getQuantity());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", dateTime=" + dateTime +
                ", customerAccountNumber=" + customerAccountNumber +
                ", counterpartAccountNumber=" + counterpartAccountNumber +
                ", direction=" + direction +
                ", currency=" + currency +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }
}
