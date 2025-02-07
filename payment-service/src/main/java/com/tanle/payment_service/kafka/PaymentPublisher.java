package com.tanle.payment_service.kafka;

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
        //param 2 is key, if I set key, kafka will hash key. With same key, message will publish to same partition
        //default is round-robin
        kafkaTemplate.send(TOPIC, String.valueOf(paymentEvent.getPaymentRequestDto().getUserId()), paymentEvent);
        System.out.println("Payment event published: " + paymentEvent);
    }
}
