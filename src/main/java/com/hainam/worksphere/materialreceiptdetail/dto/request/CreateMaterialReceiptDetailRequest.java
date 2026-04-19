package com.hainam.worksphere.materialreceiptdetail.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaterialReceiptDetailRequest {

    @NotNull(message = "Receipt id is required")
    private UUID receiptId;

    @NotBlank(message = "Item type is required")
    private String itemType;

    @NotNull(message = "Item id is required")
    private UUID itemId;

    @NotNull(message = "Quantity is required")
    private Double quantity;

    private Double unitPrice;
    private Double lineTotal;
}
