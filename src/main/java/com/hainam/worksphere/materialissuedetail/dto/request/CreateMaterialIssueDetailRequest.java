package com.hainam.worksphere.materialissuedetail.dto.request;

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
public class CreateMaterialIssueDetailRequest {

    @NotNull(message = "Issue id is required")
    private UUID issueId;

    @NotNull(message = "Item id is required")
    private UUID itemId;

    private String unit;

    @NotNull(message = "Quantity is required")
    private Double quantity;

    private Double unitPrice;
    private String reason;
    private Double lineTotal;
}
