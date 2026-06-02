package com.hainam.worksphere.pigimport.dto.request;

import jakarta.validation.constraints.Size;
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
public class CreatePigImportInvoiceDetailPigRequest {

    @Size(max = 50)
    private String earTag;

    private Integer nippleCount;

    private Double birthWeight;

    private LocalDate birthDate;

    private UUID penId;
}
