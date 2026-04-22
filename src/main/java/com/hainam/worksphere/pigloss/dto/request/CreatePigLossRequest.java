package com.hainam.worksphere.pigloss.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Mã hao hụt không được để trống")
    private UUID pigId;
    private LocalDate lossDate;
    private String reason;
    private String note;
    private Double damageValue;
    private UUID employeeId;
}
