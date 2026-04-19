package com.hainam.worksphere.materialreceipt.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreateMaterialReceiptRequest {

    @NotBlank(message = "Receipt code is required")
    private String receiptCode;

    @NotNull(message = "Receipt date is required")
    private LocalDate receiptDate;

    @NotNull(message = "Employee id is required")
    private UUID employeeId;

    private UUID supplierId;
    private Double totalAmount;
}
