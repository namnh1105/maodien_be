package com.hainam.worksphere.livestockimport.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLivestockImportRequest {

    @NotNull(message = "Receipt date is required")
    private LocalDate receiptDate;

    @NotNull(message = "Employee id is required")
    private UUID employeeId;

    private UUID supplierId;
    private Double totalAmount;

    @Valid
    @NotEmpty(message = "Import items are required")
    private List<CreateLivestockImportItemRequest> items;
}
