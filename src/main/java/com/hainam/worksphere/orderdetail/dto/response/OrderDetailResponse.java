package com.hainam.worksphere.orderdetail.dto.response;

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
public class OrderDetailResponse {

    private UUID id;
    private UUID orderId;
    private UUID pigId;
    private String pigEarTag;
    private UUID herdId;
    private Integer quantity;
    private Double unitPrice;
    private Double extraCost;
    private Double lineTotal;
    private Instant createdAt;
    private Instant updatedAt;
}
