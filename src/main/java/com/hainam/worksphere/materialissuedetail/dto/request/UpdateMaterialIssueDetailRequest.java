package com.hainam.worksphere.materialissuedetail.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMaterialIssueDetailRequest {

    private UUID issueId;
    private UUID itemId;
    private String unit;
    private Double quantity;
    private Double unitPrice;
    private String reason;
    private Double lineTotal;
}
