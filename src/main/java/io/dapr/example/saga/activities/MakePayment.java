package io.dapr.example.saga.activities;

import io.dapr.example.saga.pojos.PaymentRequest;
import io.dapr.example.saga.service.PaymentService;
import io.dapr.workflows.runtime.WorkflowActivity;
import io.dapr.workflows.runtime.WorkflowActivityContext;

public class MakePayment implements WorkflowActivity {

    @Override
    public Object run(WorkflowActivityContext ctx) {
        PaymentRequest request = ctx.getInput(PaymentRequest.class);
        return PaymentService.createPayment(request);
    }
}