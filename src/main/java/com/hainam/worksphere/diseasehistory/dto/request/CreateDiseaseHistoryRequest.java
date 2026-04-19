package com.hainam.worksphere.diseasehistory.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDiseaseHistoryRequest {
    @NotBlank
    @Size(max = 30)
    private String historyCode;
    private UUID pigId;
    private String diseaseCode;
    private String diseaseName;
    private LocalDate sickDate;
    private LocalDate recoveryDate;
    private String severity;
    private Integer expectedTreatmentDays;
    private String status;
    private String note;
}
