package io.dapr.example.saga.activities;

import io.dapr.example.saga.pojos.BookingIdRequestPayload;
import io.dapr.example.saga.service.CabAssignmentService;
import io.dapr.workflows.runtime.WorkflowActivity;
import io.dapr.workflows.runtime.WorkflowActivityContext;

public class CancelAssignment implements WorkflowActivity {

    @Override
    public Object run(WorkflowActivityContext ctx) {
        BookingIdRequestPayload driverCancellationRequest = ctx.getInput(BookingIdRequestPayload.class);
        CabAssignmentService.cancelAssignment(driverCancellationRequest.getBookingId());
        return null;
    }
}