package io.dapr.example.saga.worker;

import io.dapr.example.saga.activities.*;
import io.dapr.example.saga.workflows.SagaWorkflow;

import io.dapr.workflows.runtime.WorkflowRuntime;
import io.dapr.workflows.runtime.WorkflowRuntimeBuilder;

public class SagaWorker {

    public static void main(String[] args) throws Exception {

        WorkflowRuntimeBuilder builder = new WorkflowRuntimeBuilder().registerWorkflow(SagaWorkflow.class);
        builder.registerActivity(CreateBooking.class);
        builder.registerActivity(CancelBooking.class);
        builder.registerActivity(AssignDriver.class);
        builder.registerActivity(CancelAssignment.class);
        builder.registerActivity(MakePayment.class);
        builder.registerActivity(CancelPayment.class);
        builder.registerActivity(ConfirmBooking.class);
        builder.registerActivity(NotifyDriver.class);
        builder.registerActivity(NotifyCustomer.class);


        // Build and then start the workflow runtime pulling and executing tasks
        try (WorkflowRuntime runtime = builder.build()) {
            System.out.println("Start workflow runtime");
            runtime.start();
        }

    }
}

