package com.hainam.worksphere.diseasehistory.dto.response;

import lombok.*;

import java.time.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseHistoryResponse {
    private UUID id;
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
    private Instant createdAt;
    private Instant updatedAt;
}
