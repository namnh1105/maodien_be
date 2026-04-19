package com.hainam.worksphere.feedingration.dto.response;

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
public class FeedingRationResponse {

    private UUID id;
    private String rationCode;
    private UUID penId;
    private LocalDate rationDate;
    private Double averageIntake;
    private String note;
    private Instant createdAt;
    private Instant updatedAt;
}
