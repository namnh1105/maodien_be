package com.hainam.worksphere.pig.dto.response;

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
public class PigResponse {

    private UUID id;
    private String pigCode;
    private String earTag;
    private Double birthWeight;
    private LocalDate birthDate;
    private String type;
    private String origin;
    private String species;
    private Integer nippleCount;
    private LocalDate herdEntryDate;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
