package com.hainam.worksphere.livestockimport.dto.response;

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
public class LivestockImportResponse {

    private UUID id;
    private LocalDate receiptDate;
    private UUID employeeId;
    private UUID supplierId;
    private Double totalAmount;
    private List<LivestockImportItemResponse> items;
    private Instant createdAt;
    private Instant updatedAt;
}
