package io.dapr.example.saga.activities;

import io.dapr.example.saga.pojos.Payment;
import io.dapr.example.saga.service.PaymentService;
import io.dapr.workflows.runtime.WorkflowActivity;
import io.dapr.workflows.runtime.WorkflowActivityContext;

public class CancelPayment implements WorkflowActivity {

    @Override
    public Object run(WorkflowActivityContext ctx) {
        Payment payment = ctx.getInput(Payment.class);
        PaymentService.cancelPayment(payment);
        return null;
    }
}