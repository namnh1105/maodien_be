package com.hainam.worksphere.order.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotBlank(message = "Order code is required")
    private String orderCode;

    @NotBlank(message = "Customer name is required")
    private String customerName;

    private String phone;
    private String email;
    private Double totalAmount;
    private String status;
}
