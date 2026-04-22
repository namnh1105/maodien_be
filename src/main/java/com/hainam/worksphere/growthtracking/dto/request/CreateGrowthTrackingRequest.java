package com.hainam.worksphere.growthtracking.dto.request;

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
public class CreateGrowthTrackingRequest {

    @NotBlank(message = "Mã tăng trưởng không được để trống")
    private UUID pigId;
    private LocalDate trackingDate;
    private Double litterLength;
    private Double chestGirth;
    private Double weight;
    private Double growthRate;
    private Double adg;
    private Double fcr;
    private String note;
}
