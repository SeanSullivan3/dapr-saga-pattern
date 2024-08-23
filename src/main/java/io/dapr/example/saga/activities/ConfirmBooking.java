package io.dapr.example.saga.activities;

import io.dapr.example.saga.pojos.Booking;
import io.dapr.example.saga.pojos.BookingIdRequestPayload;
import io.dapr.example.saga.service.BookingService;
import io.dapr.workflows.runtime.WorkflowActivity;
import io.dapr.workflows.runtime.WorkflowActivityContext;

public class ConfirmBooking implements WorkflowActivity {

    @Override
    public Object run(WorkflowActivityContext ctx) {
        BookingIdRequestPayload bookingConfirmationReq = ctx.getInput(BookingIdRequestPayload.class);
        Booking booking = BookingService.getBooking(bookingConfirmationReq.getBookingId());
        BookingService.confirmBooking(booking);
        return booking;
    }
}