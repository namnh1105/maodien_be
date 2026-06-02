package com.hainam.worksphere.pigimport.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePigImportInvoiceDetailRequest {

    private UUID breedId;

    private String breedName;

    @NotNull(message = "Pig type is required")
    private String type;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be greater than 0")
    private Double unitPrice;

    @Valid
    @NotEmpty(message = "Imported pigs are required")
    private List<CreatePigImportInvoiceDetailPigRequest> pigs;
}
