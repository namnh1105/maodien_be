package com.hainam.worksphere.livestockimport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LivestockImportItemResponse {

    private UUID id;
    private UUID receiptId;
    private UUID materialId;
    private String materialName;
    private String unit;
    private Double quantity;
    private Double unitPrice;
    private Double lineTotal;
    private Double currentQuantity;
}
