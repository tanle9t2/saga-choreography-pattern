package com.tanle.order_service.controller;

import com.tanle.order_service.dto.OrderRequestDto;
import com.tanle.order_service.dto.OrderResponseDto;
import com.tanle.order_service.entity.Order;
import com.tanle.order_service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        Order order= orderService.createOrder(orderRequestDto);
        return ResponseEntity.ok(order);
    }
    @GetMapping("/")
    public ResponseEntity<OrderResponseDto> get() {
        OrderResponseDto orderResponseDto = orderService.get();
        return ResponseEntity.ok(orderResponseDto);
    }
}
