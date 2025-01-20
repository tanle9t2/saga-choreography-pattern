package com.tanle.order_service.kafka;

import com.tanle.order_service.dto.OrderRequestDto;
import com.tanle.order_service.event.OrderEvent;
import com.tanle.order_service.event.OrderStatus;
import com.tanle.order_service.service.OrderService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderProducer {
    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;

    private final String TOPIC = "order-topic";

    public OrderProducer(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOrderConfirmation(OrderRequestDto orderRequestDto, OrderStatus orderStatus) {
        System.out.println("publish order event");
        OrderEvent orderEvent = new OrderEvent(orderRequestDto, orderStatus);
        kafkaTemplate.send(TOPIC, orderEvent);
    }
}
