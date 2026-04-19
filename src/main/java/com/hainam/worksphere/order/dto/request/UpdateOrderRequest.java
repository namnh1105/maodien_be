package com.hainam.worksphere.order.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderRequest {

    private String orderCode;
    private String customerName;
    private String phone;
    private String email;
    private Double totalAmount;
    private String status;
}
