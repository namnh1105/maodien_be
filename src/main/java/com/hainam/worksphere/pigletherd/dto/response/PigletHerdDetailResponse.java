package com.hainam.worksphere.pigletherd.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PigletHerdDetailResponse {

    private PigletHerdResponse herd;
    private List<PigletHerdGrowthResponse> growthHistory;
    private List<PigletHerdMovementResponse> movementHistory;
}
