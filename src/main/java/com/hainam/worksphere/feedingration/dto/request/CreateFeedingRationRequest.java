package com.hainam.worksphere.feedingration.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class CreateFeedingRationRequest {
    @NotNull(message = "Pen id is required")
    private UUID penId;

    @NotNull(message = "Ration date is required")
    private LocalDate rationDate;

    private Double averageIntake;
    private String note;
}
