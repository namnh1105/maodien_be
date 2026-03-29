package com.hainam.worksphere.sale.dto.request;

import jakarta.validation.constraints.NotNull;
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
public class CreateSaleRequest {

    @NotNull(message = "Customer id is required")
    private UUID customerId;

    @NotNull(message = "Pig id is required")
    private UUID pigId;

    @NotNull(message = "Sale date is required")
    private LocalDate saleDate;

    @NotNull(message = "Sale price is required")
    private Double price;

    private String note;
}
