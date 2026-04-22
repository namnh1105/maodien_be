package com.hainam.worksphere.warehouseimport.dto.response;

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
public class WarehouseImportResponse {

    private UUID id;
    private UUID warehouseId;
    private String warehouseName;
    private String itemType;
    private UUID itemId;
    private Double quantity;
    private String unit;
    private LocalDate importDate;
    private UUID supplierId;
    private String supplierName;
    private Instant createdAt;
    private Instant updatedAt;
}
