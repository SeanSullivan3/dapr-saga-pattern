package io.dapr.example.saga.pojos;

import lombok.Data;

@Data
public class CustomerNotificationRequest {
    int riderId;
    String dropOff;
    String pickUp;
    String bookingId;
}
