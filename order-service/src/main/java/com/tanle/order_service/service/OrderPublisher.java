package com.tanle.order_service.service;

import com.tanle.order_service.dto.OrderRequestDto;


import com.tanle.order_service.event.OrderEvent;
import com.tanle.order_service.event.OrderStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;
@Service
public class OrderPublisher {
    @Autowired
    private Sinks.Many<OrderEvent> orderSinks;


    public void publishOrderEvent(OrderRequestDto orderRequestDto, OrderStatus orderStatus){
        OrderEvent orderEvent=new OrderEvent(orderRequestDto,orderStatus);
        orderSinks.tryEmitNext(orderEvent);
    }
}
