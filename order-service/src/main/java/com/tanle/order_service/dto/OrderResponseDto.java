package com.tanle.order_service.dto;

import com.tanle.order_service.event.OrderStatus;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto {

    private Integer userId;
    private Integer productId;
    private Integer amount;
    private Integer orderId;
    private OrderStatus orderStatus;
}