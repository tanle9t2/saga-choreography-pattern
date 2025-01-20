package com.tanle.payment_service.event;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tanle.payment_service.dto.PaymentRequestDto;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEvent implements Event {
    private UUID eventId = UUID.randomUUID();
    private Date eventDate = new Date();
    @JsonSerialize
    private PaymentRequestDto paymentRequestDto;
    @JsonSerialize
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
