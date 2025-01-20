package com.tanle.common_dtos.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentRequestDto {
    private Integer orderId;
    private Integer userId;
    private Integer amount;
}
