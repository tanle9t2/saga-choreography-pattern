package com.tanle.common_dtos.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderRequestDto {
    private Integer id;
    private Integer productId;
    private Double amount;
    private Integer orderId;
}
