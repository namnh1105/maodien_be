package com.hainam.worksphere.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatingSuccessRateResponse {
    private long totalMatings;
    private long successfulMatings;
    private Double successRate;
}
