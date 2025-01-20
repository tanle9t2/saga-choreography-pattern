package com.tanle.order_service.java.com.tanle.common_dtos.event;



import com.tanle.order_service.dto.PaymentRequestDto;

import java.util.Date;
import java.util.UUID;

public class PaymentEvent implements Event {
    private UUID eventId= UUID.randomUUID();
    private Date eventDate=new Date();
    private PaymentRequestDto paymentRequestDto;
    private PaymentStatus paymentStatus;

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Date getDate() {
        return eventDate;
    }

    public PaymentEvent(PaymentRequestDto paymentRequestDto, PaymentStatus paymentStatus) {
        this.paymentRequestDto = paymentRequestDto;
        this.paymentStatus = paymentStatus;
    }
}
