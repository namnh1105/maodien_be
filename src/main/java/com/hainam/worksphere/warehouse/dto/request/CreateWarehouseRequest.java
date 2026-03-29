package com.hainam.worksphere.warehouse.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWarehouseRequest {

    @NotBlank(message = "Warehouse code is required")
    @Size(max = 30)
    private String warehouseCode;

    @NotBlank(message = "Warehouse name is required")
    @Size(max = 150)
    private String name;

    private String warehouseType;
}
