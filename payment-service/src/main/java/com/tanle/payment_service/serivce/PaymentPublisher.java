package com.tanle.payment_service.serivce;

import com.tanle.payment_service.event.PaymentEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentPublisher {
    @Autowired
    private KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    private final String TOPIC = "payment-topic";

    public void publishMessage(PaymentEvent paymentEvent) {
        kafkaTemplate.send(TOPIC, paymentEvent);
        System.out.println("Payment event published: " + paymentEvent);
    }
}
