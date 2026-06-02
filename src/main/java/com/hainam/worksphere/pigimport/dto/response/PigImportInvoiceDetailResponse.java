package com.hainam.worksphere.pigimport.dto.response;

import com.hainam.worksphere.pig.domain.PigType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PigImportInvoiceDetailResponse {
    private UUID id;
    private UUID invoiceId;
    private UUID breedId;
    private String breedName;
    private PigType type;
    private Integer quantity;
    private Double unitPrice;
    private Double lineTotal;
    private List<PigImportInvoiceDetailPigResponse> pigs;
    private Instant createdAt;
    private Instant updatedAt;
}
