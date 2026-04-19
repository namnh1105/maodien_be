package com.hainam.worksphere.pigletherd.dto.request;

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
public class CreatePigletHerdGrowthRequest {

    @NotNull(message = "Herd id is required")
    private UUID herdId;

    @NotNull(message = "Tracking date is required")
    private LocalDate trackingDate;

    private Double averageWeight;

    private String note;
}
