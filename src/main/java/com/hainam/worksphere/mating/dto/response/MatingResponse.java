package com.hainam.worksphere.mating.dto.response;

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
public class MatingResponse {

    private UUID id;
    private String matingCode;
    private UUID sowPigId;
    private UUID boarBreedId;
    private Double litterLength;
    private Integer matingRound;
    private UUID employeeId;
    private LocalDate matingDate;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
