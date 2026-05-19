package com.hainam.worksphere.pigloss.dto.request;

import jakarta.validation.constraints.NotNull;
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
public class CreatePigLossRequest {

    @NotNull(message = "Mã hao hụt không được để trống")
    private UUID pigId;
    private LocalDate lossDate;
    private String reason;
    private String note;
    private Double damageValue;
    private UUID employeeId;
}
