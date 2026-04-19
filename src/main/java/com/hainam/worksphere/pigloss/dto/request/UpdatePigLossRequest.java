package com.hainam.worksphere.pigloss.dto.request;

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
public class UpdatePigLossRequest {

    private UUID pigId;
    private LocalDate lossDate;
    private String reason;
    private String note;
    private Double damageValue;
    private UUID employeeId;
}
