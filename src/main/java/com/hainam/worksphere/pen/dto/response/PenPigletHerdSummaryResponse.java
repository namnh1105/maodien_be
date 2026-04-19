package com.hainam.worksphere.pen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenPigletHerdSummaryResponse {
    private UUID herdId;
    private String herdName;
    private Integer quantity;
    private Double averageBirthWeight;
}
