package com.hainam.worksphere.materialreceipt.dto.request;

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
public class UpdateMaterialReceiptRequest {

    private String receiptCode;
    private LocalDate receiptDate;
    private UUID employeeId;
    private UUID supplierId;
    private Double totalAmount;
}
