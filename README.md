## Transaction Monitoring Service API  *(manual integration tests performed in Postman)*
#####This transaction monitoring service flags suspicious wire transfers when customers are moving money into or out of their accounts


### Submit Transaction
Method **POST**

Before the wire transfer is completed the following URL should be requested <http://localhost:8080/api/transactions>
with POST request and a transaction object body
The headers should contain the header "Content-Type"  - application/json
In response the invoked method returns status "ACCEPT" or "HOLD"

please refer to the example request body below:
```json
   {
         "transactionId": "1A",
         "dateTime": null,
         "customerAccountNumber": 1,
         "counterpartAccountNumber": 2,
         "direction": "DEBIT",
         "currency": "USD",
         "quantity": 100,
         "status": null
     }
```
***

Expected status "ACCEPT" or "HOLD"

### Update Transaction Status
Method **PUT**

If transaction status equals to "HOLD" the following URL <http://localhost:8080/api/transactions> could be invoked  to adjust the transaction status from "HOLD" to "APPROVED" or "REJECTED"
based on the decision taken by the individual or other micro service
The PUT request body should contain transactionId and status ("APPROVED" or "REJECTED") that should be assigned to this transaction
The response is a boolean either true if status is adjusted or false

 
```json
{
"status":"APPROVED",
"transactionId":"1B"
}
```

Expected result "true" or "false"

### List all transactions
Method **GET**
 

By invoking the following URL <http://localhost:8080/api/transactions>  with GET request will return all transactions existed in the database

 
### Exaple of test using postman (typical flow)


Start Application by ./mvnw spring-boot:run

Create new **POST** request to <http://localhost:8080/api/transactions> with the folowing body

```json
   {
         "transactionId": "1A",
         "dateTime": null,
         "customerAccountNumber": 1,
         "counterpartAccountNumber": 101,
         "direction": "CREDIT",
         "currency": "USD",
         "quantity": 100000,
         "status": null
     }
```
The Direction is CREDIT, expected response "ACCEPT"

Create new **POST** request to <http://localhost:8080/api/transactions> with the folowing body

```json
   {
         "transactionId": "2A",
         "dateTime": null,
         "customerAccountNumber": 2,
         "counterpartAccountNumber": 102,
         "direction": "DEBIT",
         "currency": "USD",
         "quantity": 100,
         "status": null
     }
```
The amount of transaction in USD is less than a limit, expected result "ACCEPT"
 

Create new **POST** request to <http://localhost:8080/api/transactions> with the following body
 
 ```json
    {
          "transactionId": "3A",
          "dateTime": null,
          "customerAccountNumber": 3,
          "counterpartAccountNumber": 103,
          "direction": "DEBIT",
          "currency": "USD",
          "quantity": 1500,
          "status": null
      }
 ```
 The amount of transaction in USD is more than a limit, expected result "HOLD"
  
Create new **POST** request to <http://localhost:8080/api/transactions> with the following body
 
 ```json
    {
          "transactionId": "4A",
          "dateTime": null,
          "customerAccountNumber": 1,
          "counterpartAccountNumber": 103,
          "direction": "DEBIT",
          "currency": "USD",
          "quantity": 100,
          "status": null
      }
 ```
 
The amount of transaction in USD is  a limit, but the was a transaction before with the same conterparty 
and it has status HOLD or REJECTED, expected result "HOLD"

Create new **PUT** request to <http://localhost:8080/api/transactions> with the following body
 
 ```json
 {
 "status":"APPROVED",
 "transactionId":"3A"
 }
 ```
 
Expected response "true"

Create new **GET** request to <http://localhost:8080/api/transactions> 

Now the status of the transaction with id 3A changed to Approved


Create new **POST** request to <http://localhost:8080/api/transactions> with the following body
 
 ```json
    {
          "transactionId": "5A",
          "dateTime": null,
          "customerAccountNumber": 3,
          "counterpartAccountNumber": 103,
          "direction": "DEBIT",
          "currency": "USD",
          "quantity": 100500,
          "status": null
      }
 ```
Expected response "ACCEPT", because the previous transaction with the same pair of accounts has status "APPROVED"