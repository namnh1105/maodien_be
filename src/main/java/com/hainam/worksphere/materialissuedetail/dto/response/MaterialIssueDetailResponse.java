package com.hainam.worksphere.materialissuedetail.dto.response;

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
public class MaterialIssueDetailResponse {

    private UUID id;
    private UUID issueId;
    private String unit;
    private Double quantity;
    private Instant createdAt;
    private Instant updatedAt;
}
