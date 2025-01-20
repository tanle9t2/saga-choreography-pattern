package com.tanle.order_service.config;

import com.tanle.order_service.event.OrderEvent;
import com.tanle.order_service.event.PaymentEvent;
import com.tanle.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.function.Supplier;

@Configuration
public class OrderPublisherConfig {
    @Autowired
    private OrderService orderService;

    @Bean
    public Sinks.Many<OrderEvent> orderSinks() {
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    @Bean
    public Supplier<Flux<OrderEvent>> orderSupplier(Sinks.Many<OrderEvent> sinks) {
        return sinks::asFlux;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @KafkaListener(topics = "payment-topic", groupId = "saga-service")
    public void consumeTopicPayment(List<PaymentEvent> paymentEvents) {
        Flux.fromIterable(paymentEvents)
                .flatMap(paymentEvent -> Mono.fromRunnable(() -> {
                    orderService.handlePaymentEvent(paymentEvent, o -> o.setPaymentStatus(paymentEvent.getPaymentStatus()));
                }))
                .subscribe();
        System.out.println("paymentEvents = " + paymentEvents);
    }
}
