package io.dapr.example.saga.workflows;

import io.dapr.example.saga.activities.CreateBooking;
import io.dapr.example.saga.activities.*;
import io.dapr.example.saga.pojos.*;

import io.dapr.workflows.Workflow;
import io.dapr.workflows.WorkflowStub;
import io.dapr.workflows.saga.Saga;
import io.dapr.workflows.saga.SagaOption;

import com.microsoft.durabletask.Task;


public class SagaWorkflow extends Workflow {
    @Override
    public WorkflowStub create() {
        return ctx -> {
            Saga saga = new Saga(new SagaOption.Builder().build());

            BookingRequest bookingRequest = ctx.getInput(BookingRequest.class);

            // Book a ride
            BookingIdRequestPayload bookingId =
                    ctx.callActivity(
                            CreateBooking.class.getName(),
                            bookingRequest,
                            BookingIdRequestPayload.class
                    ).await();
            saga.registerCompensation(CancelBooking.class.getName(), bookingId);
            if (bookingId.getBookingId() == null) {
                saga.compensate(ctx);
                ctx.complete("Booking creation failure");
            }

            // Assign a driver
            CabAssignment cabAssignment =
                    ctx.callActivity(
                            AssignDriver.class.getName(),
                            bookingId,
                            CabAssignment.class
                    ).await();
            saga.registerCompensation(CancelAssignment.class.getName(), bookingId);
            if (cabAssignment == null) {
                saga.compensate(ctx);
                ctx.complete("Driver assignment failure");
            }

            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setBookingId(bookingId.getBookingId());
            paymentRequest.setRiderId(bookingRequest.getRiderId());

            // Make payment
            Payment payment = ctx.callActivity(
                    MakePayment.class.getName(),
                    paymentRequest,
                    Payment.class
            ).await();
            saga.registerCompensation(CancelPayment.class.getName(), payment);
            if (payment.getStatus() == Payment.Status.FAILED) {
                saga.compensate(ctx);
                ctx.complete("Payment failure");
            }

            // Confirm Booking
            Booking confirmedBooking = ctx.callActivity(
                    ConfirmBooking.class.getName(),
                    bookingId,
                    Booking.class
            ).await();
            if (confirmedBooking.getStatus() != Booking.Status.CONFIRMED) {
                saga.compensate(ctx);
                ctx.complete("Booking confirmation failure");
            }

            // Notify customer and driver
            Task<Integer> driver = ctx.callActivity(
                    NotifyDriver.class.getName(),
                    confirmedBooking,
                    Integer.class
            );
            Task<Integer> rider = ctx.callActivity(
                    NotifyCustomer.class.getName(),
                    confirmedBooking,
                    Integer.class
            );
            driver.await();
            rider.await();

            ctx.complete("Successful Ride");
        };
    }
}
