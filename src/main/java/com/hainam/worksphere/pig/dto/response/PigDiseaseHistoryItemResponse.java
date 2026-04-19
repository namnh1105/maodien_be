package com.hainam.worksphere.pig.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PigDiseaseHistoryItemResponse {
    private UUID id;
    private String diseaseCode;
    private String diseaseName;
    private LocalDate sickDate;
    private LocalDate recoveryDate;
    private String status;
}
