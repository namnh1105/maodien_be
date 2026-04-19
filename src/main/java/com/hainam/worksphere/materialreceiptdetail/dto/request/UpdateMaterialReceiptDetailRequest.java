package com.hainam.worksphere.materialreceiptdetail.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMaterialReceiptDetailRequest {

    private UUID receiptId;
    private String itemType;
    private UUID itemId;
    private Double quantity;
    private Double unitPrice;
    private Double lineTotal;
}
