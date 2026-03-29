package com.hainam.worksphere.sale.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSaleRequest {

    private UUID customerId;
    private UUID pigId;
    private LocalDate saleDate;
    private Double price;
    private String note;
}
