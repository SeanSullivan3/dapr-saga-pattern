# Dapr Saga Pattern Example
This project is a Dapr implementation of an example [﻿saga pattern by Orkes Conductor](https://github.com/conductor-sdk/conductor-examples-saga-pattern) that I adpated from [Temporal saga pattern](https://github.com/SeanSullivan3/temporal-saga-pattern) in Java. In this example saga, a customer will book a cab through a ride service in a four step process. First, the booking is created and put in the booking database. Second, a random cab driver is assigned for the booking and the cab details are put in the cab database. Third, the program will find the customer's payment information from the rider database, put the transaction information into the payment database, and simulate the payment processing. Lastly, the booking will be confirmed and a notifcation will be sent to the driver and customer. If at any point in the workflow the application encounters an error, all completed steps will be compensated for using Dapr's Saga framework.

## Run Instructions
### Environment setup
1. Install JAVA 17 - [﻿https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) 
2. Install Maven 4.0.0 - [﻿https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi) 
3. Install sqlite - [﻿https://www.tutorialspoint.com/sqlite/sqlite_installation.htm](https://www.tutorialspoint.com/sqlite/sqlite_installation.htm) .
If using brew, you can just run `brew install sqlite`
4. Install and Initialize Dapr - [Install](https://docs.dapr.io/getting-started/install-dapr-cli/) -- [Initialize](https://docs.dapr.io/getting-started/install-dapr-selfhost/)
### Running the application
1. Clone this repository
2. Open a terminal and start the temporal server.
```bash
dapr run --app-id sagaworker --dapr-grpc-port 50001
```
3. In second terminal, compile the project and start the Worker.
- This terminal will display the log messages as the saga completes.
```bash
mvn clean compile
mvn compile exec:java -Dexec.mainClass="io.dapr.example.saga.worker.SagaWorker"
```
4. In a third terminal, start the spring boot application.
```bash
mvn compile exec:java -Dexec.mainClass="io.dapr.example.saga.SagaApplication"
```
5. In a fourth terminal, run the booking creation command below.  
  
### Booking creation
To trigger the workflow and begin the saga, use the command below

```
curl --location 'http://localhost:8080/triggerRideBookingFlow' \
--header 'Content-Type: application/json' \
--data '{
  "pickUpLocation": "150 East 52nd Street, New York, NY 10045",
  "dropOffLocation": "120 West 81st Street, New York, NY 10012",
  "riderId": 1
}'
```
