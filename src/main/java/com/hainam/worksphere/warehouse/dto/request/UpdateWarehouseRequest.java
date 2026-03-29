package com.hainam.worksphere.warehouse.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWarehouseRequest {

    @Size(max = 150)
    private String name;

    private String warehouseType;
}
