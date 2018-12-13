package com.jpmprgan.frauddetection.util;

import com.jpmprgan.frauddetection.domain.Status;

/**
 *    An object that is used to update status of the given transaction with status "HOLD"
 *
 */


public class UpdateStatusRequest {
    
    private Status status;
    private String transactionId;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public UpdateStatusRequest(Status status, String transactionId) {
        this.status = status;
        this.transactionId = transactionId;
    }

    public UpdateStatusRequest() {
    }

    @Override
    public String toString() {
        return "UpdateStatusRequest{" +
                "status=" + status +
                ", transactionId='" + transactionId + '\'' +
                '}';
    }
}
