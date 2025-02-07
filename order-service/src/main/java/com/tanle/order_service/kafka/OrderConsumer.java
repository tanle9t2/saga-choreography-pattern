package com.tanle.order_service.kafka;

import com.tanle.order_service.event.PaymentEvent;
import com.tanle.order_service.service.OrderService;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
@Service
@Slf4j
@RequiredArgsConstructor
public class OrderConsumer {
    @Autowired
    private OrderService orderService;
    @KafkaListener(topics = "payment-topic", groupId = "order")
    public void consumeTopicPaymentP1(List<PaymentEvent> paymentEvents) {
        Flux.fromIterable(paymentEvents)
                .flatMap(paymentEvent -> Mono.fromRunnable(() -> {
                    orderService.handlePaymentEvent(paymentEvent, o -> o.setPaymentStatus(paymentEvent.getPaymentStatus()));
                }))
                .subscribe();
        System.out.println("Process event from p1 paymentEvents = " + paymentEvents);
    }
    @KafkaListener(topics = "payment-topic", groupId = "order")
    public void consumeTopicPaymentP2(List<PaymentEvent> paymentEvents) {
        Flux.fromIterable(paymentEvents)
                .flatMap(paymentEvent -> Mono.fromRunnable(() -> {
                    orderService.handlePaymentEvent(paymentEvent, o -> o.setPaymentStatus(paymentEvent.getPaymentStatus()));
                }))
                .subscribe();
        System.out.println("Process event from p2 paymentEvents = " + paymentEvents );
    }
}
