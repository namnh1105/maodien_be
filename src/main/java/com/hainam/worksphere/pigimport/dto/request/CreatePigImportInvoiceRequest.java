package com.hainam.worksphere.pigimport.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
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
public class CreatePigImportInvoiceRequest {

    @NotBlank(message = "Invoice code is required")
    private String invoiceCode;

    private UUID supplierId;

    private String supplierName;

    @NotNull(message = "Import date is required")
    private LocalDate importDate;

    @Valid
    @NotEmpty(message = "Invoice details are required")
    private List<CreatePigImportInvoiceDetailRequest> details;
}
