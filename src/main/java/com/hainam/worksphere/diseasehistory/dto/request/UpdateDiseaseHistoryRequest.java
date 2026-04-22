package com.hainam.worksphere.diseasehistory.dto.request;

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
public class UpdateDiseaseHistoryRequest {

    private UUID pigId;
    private String diseaseName;
    private LocalDate sickDate;
    private LocalDate recoveryDate;
    private String severity;
    private Integer expectedTreatmentDays;
    private String status;
    private String note;
}
