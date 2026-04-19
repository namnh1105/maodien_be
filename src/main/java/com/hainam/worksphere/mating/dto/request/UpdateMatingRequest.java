package com.hainam.worksphere.mating.dto.request;

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
public class UpdateMatingRequest {

    private UUID sowPigId;

    private UUID boarBreedId;

    private Double litterLength;

    private Integer matingRound;

    private UUID employeeId;

    private LocalDate matingDate;

    @Size(max = 50)
    private String status;
}
