package com.hainam.worksphere.orderdetail.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDetailRequest {

    @NotNull(message = "Order id is required")
    private UUID orderId;

    private UUID pigId;
    private UUID herdId;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private Double unitPrice;
    private Double extraCost;
    private Double lineTotal;
}
