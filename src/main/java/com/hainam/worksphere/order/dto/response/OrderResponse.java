package com.hainam.worksphere.order.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private UUID id;
    private String orderCode;
    private String customerName;
    private String phone;
    private String email;
    private Double totalAmount;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
