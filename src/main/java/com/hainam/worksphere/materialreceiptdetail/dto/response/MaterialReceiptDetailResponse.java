package com.hainam.worksphere.materialreceiptdetail.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialReceiptDetailResponse {

    private UUID id;
    private UUID receiptId;
    private String itemType;
    private UUID itemId;
    private Double quantity;
    private Double unitPrice;
    private Double lineTotal;
    private Instant createdAt;
    private Instant updatedAt;
}
