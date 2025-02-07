package com.tanle.payment_service.serivce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanle.payment_service.event.PaymentEvent;
import com.tanle.payment_service.kafka.PaymentPublisher;
import com.tanle.payment_service.repo.OutBoxRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
@Service
@EnableScheduling
public class PaymentPollerService {
    @Autowired
    private OutBoxRepo outBoxRepo;

    @Autowired
    private PaymentPublisher paymentPublisher;
    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(fixedRate = 6000)
    public void pollOutBoxMessageAndPublish() {
        Flux.fromIterable(outBoxRepo.findOutBoxesByNotProcess())
                .subscribe(outBox -> {
                    try {
                        PaymentEvent paymentEvent = objectMapper.treeToValue(outBox.getPayload(), PaymentEvent.class);
                        paymentPublisher.publishMessage(paymentEvent);
                        outBox.setProcess(true);
                        outBoxRepo.save(outBox);
                    } catch (Exception e) {
                        System.out.printf(e.getMessage());
                    }
                });

    }
}
