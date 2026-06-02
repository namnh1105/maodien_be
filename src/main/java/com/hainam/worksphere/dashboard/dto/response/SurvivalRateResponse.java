package com.hainam.worksphere.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurvivalRateResponse {
    private long bornCount;
    private long aliveCount;
    private long crushedCount;
    private long stillbornCount;
    private long deformedCount;
    private Double aliveRate;
    private Double crushedRate;
    private Double stillbornRate;
    private Double deformedRate;
}
