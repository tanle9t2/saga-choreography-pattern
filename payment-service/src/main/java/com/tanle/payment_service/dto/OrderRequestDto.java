package com.tanle.payment_service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class OrderRequestDto {
    private Integer userId;
    private Integer productId;
    private Double amount;
    private Integer orderId;
}
