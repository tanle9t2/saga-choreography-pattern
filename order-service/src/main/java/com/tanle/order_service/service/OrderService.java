package com.tanle.order_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tanle.order_service.dto.OrderRequestDto;
import com.tanle.order_service.dto.OrderResponseDto;
import com.tanle.order_service.entity.Order;
import com.tanle.order_service.entity.OutBox;
import com.tanle.order_service.event.OrderStatus;
import com.tanle.order_service.event.PaymentEvent;
import com.tanle.order_service.event.PaymentStatus;
import com.tanle.order_service.kafka.OrderProducer;
import com.tanle.order_service.repo.OrderRepo;

import com.tanle.order_service.repo.OutBoxRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.function.Consumer;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;
    @Autowired
    private OutBoxRepo outBoxRepo;
    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    public Order createOrder(OrderRequestDto orderRequestDto) {
        Order order = orderRepo.save(convertDtoToEntity(orderRequestDto));
        orderRequestDto.setOrderId(order.getId());
//        //produce kafka event with status ORDER_CREATED
//        orderStatusPublisher.publishOrderEvent(orderRequestDto, OrderStatus.ORDER_CREATED);
        OutBox outBox = OutBox.builder()
                .aggregateId(order.getId().toString())
                .createdAt(new Date())
                .payload(objectMapper.valueToTree(orderRequestDto))
                .isProcess(false)
                .build();
        outBoxRepo.save(outBox);
        return order;
    }
    public OrderResponseDto get() {
        Order order = orderRepo.findById(1353).get();
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(order.getId());
        orderResponseDto.setOrderStatus(order.getOrderStatus());

        return orderResponseDto;
    }

    @Transactional
    public PaymentEvent handlePaymentEvent(PaymentEvent paymentEvent, Consumer<Order> consumer) {
        orderRepo.findById(paymentEvent.getPaymentRequestDto().getOrderId())
                .ifPresent(consumer.andThen((o) -> {
                    if (paymentEvent.getPaymentStatus().equals(PaymentStatus.PAYMENT_COMPLETED)) {
                        updateOrderStatus(o, OrderStatus.ORDER_COMPLETED);
                    } else {
                        updateOrderStatus(o, OrderStatus.ORDER_CANCELLED);
                    }
                }));
        return paymentEvent;
    }

    @Transactional
    public void updateOrderStatus(Order order, OrderStatus orderStatus) {
        order.setOrderStatus(orderStatus);
        orderRepo.save(order);
    }

    private Order convertDtoToEntity(OrderRequestDto dto) {
        Order purchaseOrder = new Order();
        purchaseOrder.setProductId(dto.getProductId());
        purchaseOrder.setUserId(dto.getUserId());
        purchaseOrder.setOrderStatus(OrderStatus.ORDER_CREATED);
        purchaseOrder.setPrice(dto.getAmount());
        return purchaseOrder;
    }
}
