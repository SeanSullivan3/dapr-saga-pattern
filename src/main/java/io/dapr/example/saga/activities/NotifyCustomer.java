package io.dapr.example.saga.activities;

import io.dapr.example.saga.pojos.Booking;
import io.dapr.workflows.runtime.WorkflowActivity;
import io.dapr.workflows.runtime.WorkflowActivityContext;

public class NotifyCustomer implements WorkflowActivity {

    @Override
    public Object run(WorkflowActivityContext ctx) {
        Booking booking = ctx.getInput(Booking.class);
        return booking.getRiderId();
    }
}
