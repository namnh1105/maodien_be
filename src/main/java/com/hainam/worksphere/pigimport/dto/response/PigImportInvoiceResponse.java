package com.hainam.worksphere.pigimport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PigImportInvoiceResponse {
    private UUID id;
    private String invoiceCode;
    private UUID supplierId;
    private String supplierName;
    private LocalDate importDate;
    private Integer totalQuantity;
    private Double totalAmount;
    private List<PigImportInvoiceDetailResponse> details;
    private Instant createdAt;
    private Instant updatedAt;
}
