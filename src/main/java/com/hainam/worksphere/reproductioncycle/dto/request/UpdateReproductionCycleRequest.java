package com.hainam.worksphere.reproductioncycle.dto.request;

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
public class UpdateReproductionCycleRequest {

    private UUID matingId;
    private LocalDate conceptionDate;
    private LocalDate expectedFarrowDate;
    private LocalDate actualFarrowDate;

    @Size(max = 50)
    private String status;

    private Integer bornCount;
    private Integer aliveCount;
    private Integer deadCount;
    private Double averageWeight;
}
