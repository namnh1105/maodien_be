package com.hainam.worksphere.pig.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PigDetailResponse {
    private PigResponse pig;
    private UUID currentPenId;
    private String currentPenName;
    private LocalDate penEntryDate;
    private Double currentWeight;
    private Double adg;
    private Double fcr;
    private List<PigGrowthItemResponse> growthHistory;
    private List<PigDiseaseHistoryItemResponse> diseaseHistory;
    private List<PigVaccinationItemResponse> vaccinations;
}
