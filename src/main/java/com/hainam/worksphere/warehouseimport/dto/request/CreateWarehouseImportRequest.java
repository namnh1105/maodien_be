package com.hainam.worksphere.warehouseimport.dto.request;

import jakarta.validation.constraints.NotNull;
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
public class CreateWarehouseImportRequest {

    @NotNull(message = "Warehouse id is required")
    private UUID warehouseId;

    @NotNull(message = "Item type is required")
    private String itemType;

    @NotNull(message = "Item id is required")
    private UUID itemId;

    @NotNull(message = "Quantity is required")
    private Double quantity;

    @Size(max = 50)
    private String unit;

    @NotNull(message = "Import date is required")
    private LocalDate importDate;

    private UUID supplierId;
}
