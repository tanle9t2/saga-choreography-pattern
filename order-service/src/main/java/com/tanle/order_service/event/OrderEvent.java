package com.tanle.order_service.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tanle.order_service.dto.OrderRequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent implements Event {
    private UUID eventId = UUID.randomUUID();
    private Date eventDate = new Date();
    @JsonSerialize
    private OrderRequestDto orderRequestDto;
    @JsonSerialize
    private OrderStatus orderStatus;

    @Override
    public UUID getEventId() {
        return eventId;
    }

    @Override
    public Date getDate() {
        return eventDate;
    }

    public OrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {
        this.orderRequestDto = orderRequestDto;
        this.orderStatus = orderStatus;
    }
}
