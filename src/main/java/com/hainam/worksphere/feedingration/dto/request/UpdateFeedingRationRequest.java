package com.hainam.worksphere.feedingration.dto.request;

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
public class UpdateFeedingRationRequest {
    private UUID penId;
    private LocalDate rationDate;
    private Double averageIntake;
    private String note;
}
