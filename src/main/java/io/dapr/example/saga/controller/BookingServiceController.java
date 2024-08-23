package io.dapr.example.saga.controller;

import io.dapr.example.saga.pojos.BookingRequest;
import io.dapr.example.saga.workflows.SagaWorkflow;

import io.dapr.workflows.client.DaprWorkflowClient;

import io.dapr.workflows.client.WorkflowInstanceStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeoutException;

@RestController
public class BookingServiceController {

    @PostMapping(value = "/triggerRideBookingFlow", produces = "application/json")
    public ResponseEntity<String> triggerRideBookingFlow(@RequestBody BookingRequest bookingRequest) {

        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        bookingRequest.setBookingId(uuidAsString);

        try(DaprWorkflowClient client = new DaprWorkflowClient()) {

            String instanceId = client.scheduleNewWorkflow(SagaWorkflow.class, bookingRequest);
            WorkflowInstanceStatus workflowInstanceStatus =
                    client.waitForInstanceCompletion(instanceId, null, true);

            String result = workflowInstanceStatus.readOutputAs(String.class);
            return ResponseEntity.ok(result);
        }
        catch (TimeoutException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
