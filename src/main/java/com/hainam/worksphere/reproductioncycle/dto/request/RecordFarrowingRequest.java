package com.hainam.worksphere.reproductioncycle.dto.request;

import jakarta.validation.constraints.NotNull;
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
public class RecordFarrowingRequest {

    @NotNull(message = "Reproduction cycle id is required")
    private UUID id;

    private LocalDate actualFarrowDate;

    @Size(max = 50)
    private String status;

    private Integer bornCount;
    private Integer aliveCount;
    private Integer deadCount;
    private Integer crushedCount;
    private Integer deformedCount;
    private Double averageWeight;
}
