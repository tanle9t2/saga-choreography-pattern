package com.tanle.payment_service.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tanle.payment_service.dto.OrderRequestDto;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent implements Event {
    private UUID eventId = UUID.randomUUID();
    private Date eventDate = new Date();
    private OrderRequestDto orderRequestDto;
    private OrderStatus orderStatus;



    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Date getDate() {
        return eventDate;
    }

}
