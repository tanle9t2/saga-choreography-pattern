package com.tanle.order_service.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {
    private Integer orderId;
    private Integer userId;
    private Double amount;

}
