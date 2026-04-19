package com.hainam.worksphere.orderdetail.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOrderDetailRequest {

    private UUID orderId;
    private UUID pigId;
    private UUID herdId;
    private Integer quantity;
    private Double unitPrice;
    private Double extraCost;
    private Double lineTotal;
}
