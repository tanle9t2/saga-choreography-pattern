package com.tanle.payment_service.config;

import com.tanle.payment_service.event.OrderEvent;
import com.tanle.payment_service.event.OrderStatus;
import com.tanle.payment_service.event.PaymentEvent;
import com.tanle.payment_service.serivce.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;

@Configuration
public class PaymentConsumerConfig {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;


    @KafkaListener(topics = "order-topic", groupId = "saga-service")
    public void consumeEventAndPublisheTopic(List<OrderEvent> orderEvents) {
//        System.out.println(orderEvents.getEventId());
        Flux.fromIterable(orderEvents)
                .flatMap(this::processPayment)
                .subscribe(this::handlePaymentEvent);
    }

    private Mono<PaymentEvent> processPayment(OrderEvent orderEvent) {
        if (OrderStatus.ORDER_CREATED.equals(orderEvent.getOrderStatus())) {
            return Mono.fromSupplier(() -> paymentService.newOrderEvent(orderEvent));
        } else {
            return Mono.fromRunnable(() -> paymentService.cancelOrderEvent(orderEvent));
        }
    }
    private void handlePaymentEvent(PaymentEvent paymentEvent) {
        kafkaTemplate.send("payment-topic", paymentEvent);
        System.out.println("Payment event published: " + paymentEvent);
    }
}
