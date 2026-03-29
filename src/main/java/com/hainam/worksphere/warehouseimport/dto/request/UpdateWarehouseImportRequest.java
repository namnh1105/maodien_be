package com.hainam.worksphere.warehouseimport.dto.request;

import jakarta.validation.constraints.Size;
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
public class UpdateWarehouseImportRequest {

    private UUID warehouseId;
    private String itemType;
    private UUID itemId;
    private Double quantity;

    @Size(max = 50)
    private String unit;

    private LocalDate importDate;
    private UUID supplierId;
}
