package com.hainam.worksphere.materialreceipt.dto.response;

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
public class MaterialReceiptResponse {

    private UUID id;
    private String receiptCode;
    private LocalDate receiptDate;
    private UUID employeeId;
    private UUID supplierId;
    private Double totalAmount;
    private Instant createdAt;
    private Instant updatedAt;
}
