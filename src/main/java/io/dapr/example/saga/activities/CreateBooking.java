package io.dapr.example.saga.activities;

import io.dapr.example.saga.pojos.BookingIdRequestPayload;
import io.dapr.example.saga.pojos.BookingRequest;
import io.dapr.example.saga.service.BookingService;
import io.dapr.workflows.runtime.WorkflowActivity;
import io.dapr.workflows.runtime.WorkflowActivityContext;

public class CreateBooking implements WorkflowActivity {

    @Override
    public Object run(WorkflowActivityContext ctx) {
        BookingRequest request = ctx.getInput(BookingRequest.class);
        String bookingId = BookingService.createBooking(request);
        BookingIdRequestPayload result = new BookingIdRequestPayload();
        result.setBookingId(bookingId);
        return result;
    }
}
