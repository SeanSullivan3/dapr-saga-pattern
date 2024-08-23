package io.dapr.example.saga.activities;

import io.dapr.example.saga.pojos.BookingIdRequestPayload;
import io.dapr.example.saga.service.CabAssignmentService;
import io.dapr.workflows.runtime.WorkflowActivity;
import io.dapr.workflows.runtime.WorkflowActivityContext;

public class AssignDriver implements WorkflowActivity {

    @Override
    public Object run(WorkflowActivityContext ctx) {
        BookingIdRequestPayload id = ctx.getInput(BookingIdRequestPayload.class);
        return CabAssignmentService.assignDriver(id.getBookingId());
    }
}