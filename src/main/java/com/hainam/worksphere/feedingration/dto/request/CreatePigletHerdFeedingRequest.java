package com.hainam.worksphere.feedingration.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class CreatePigletHerdFeedingRequest {

    @NotNull(message = "Piglet herd id is required")
    private UUID herdId;

    @NotNull(message = "Feed id is required")
    private UUID feedId;

    @NotNull(message = "Feed amount is required")
    @Positive(message = "Feed amount must be greater than 0")
    private Double feedAmount;

    @NotNull(message = "Feeding date is required")
    private LocalDate feedingDate;

    private String note;
}
