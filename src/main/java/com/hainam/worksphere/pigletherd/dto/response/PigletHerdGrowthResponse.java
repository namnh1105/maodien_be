package com.hainam.worksphere.pigletherd.dto.response;

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
public class PigletHerdGrowthResponse {

    private UUID id;
    private UUID herdId;
    private LocalDate trackingDate;
    private Double averageWeight;
    private String note;
    private Instant createdAt;
    private Instant updatedAt;
}
