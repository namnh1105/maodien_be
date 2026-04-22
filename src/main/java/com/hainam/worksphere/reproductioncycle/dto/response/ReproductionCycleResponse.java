package com.hainam.worksphere.reproductioncycle.dto.response;

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
public class ReproductionCycleResponse {

    private UUID id;
    private UUID matingId;
    private LocalDate conceptionDate;
    private LocalDate expectedFarrowDate;
    private LocalDate actualFarrowDate;
    private String status;
    private Integer bornCount;
    private Integer aliveCount;
    private Integer deadCount;
    private Double averageWeight;
    private Instant createdAt;
    private Instant updatedAt;
}
