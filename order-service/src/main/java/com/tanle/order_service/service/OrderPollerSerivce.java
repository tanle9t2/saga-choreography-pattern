package com.tanle.order_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanle.order_service.dto.OrderRequestDto;
import com.tanle.order_service.dto.OrderResponseDto;
import com.tanle.order_service.entity.OutBox;
import com.tanle.order_service.event.OrderStatus;
import com.tanle.order_service.kafka.OrderProducer;
import com.tanle.order_service.repo.OutBoxRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
@EnableScheduling
public class OrderPollerSerivce {
    @Autowired
    private OutBoxRepo outBoxRepo;
    @Autowired
    private OrderProducer orderProducer;
    @Autowired
    private ObjectMapper objectMapper;

    @Scheduled(fixedRate = 6000)
    public void pollOutBoxMessageAndPublish() {
        System.out.println("run every 10s");
        Flux.fromIterable(outBoxRepo.findOutBoxesByNotProcess())
                .subscribe(outBox -> {
                    try {
                        OrderRequestDto orderRequestDto = objectMapper.treeToValue(outBox.getPayload(), OrderRequestDto.class);
                        orderProducer.sendOrderConfirmation(orderRequestDto, OrderStatus.ORDER_CREATED);
                        outBox.setProcess(true);
                        outBoxRepo.save(outBox);
                    } catch (Exception e) {
                        System.out.printf(e.getMessage());
                    }
                });

    }
}
