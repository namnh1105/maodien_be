package com.hainam.worksphere.pigloss.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PigLossResponse {

    private UUID id;
    private UUID pigId;
    private String pigEarTag;
    private LocalDate lossDate;
    private String reason;
    private String note;
    private Double damageValue;
    private UUID employeeId;
    private Instant createdAt;
    private Instant updatedAt;
}
