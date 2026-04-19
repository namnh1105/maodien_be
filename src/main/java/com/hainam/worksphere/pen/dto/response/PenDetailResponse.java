package com.hainam.worksphere.pen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenDetailResponse {
    private UUID id;
    private String penCode;
    private String name;
    private UUID areaId;
    private Double area;
    private Integer pigCount;
    private Integer pigletCount;
    private Double latestAverageIntake;
    private List<PenPigSummaryResponse> pigs;
    private List<PenPigletHerdSummaryResponse> pigletHerds;
}
