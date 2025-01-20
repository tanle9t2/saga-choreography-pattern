package com.tanle.order_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDto {
    private Integer userId;
    private Integer productId;
    private Double amount;
    private Integer orderId;


}
