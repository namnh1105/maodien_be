package com.hainam.worksphere.sale.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponse {

    private UUID id;
    private UUID customerId;
    private String customerName;
    private UUID pigId;
    private String pigEarTag;
    private LocalDate saleDate;
    private Double price;
    private String note;
    private Instant createdAt;
    private Instant updatedAt;
}
