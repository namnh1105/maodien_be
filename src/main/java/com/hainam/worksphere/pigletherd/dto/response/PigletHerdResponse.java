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
public class PigletHerdResponse {

    private UUID id;
    private String herdCode;
    private UUID motherId;
    private String motherCode;
    private UUID fatherId;
    private String fatherCode;
    private Integer quantity;
    private String genderNote;
    private Double averageBirthWeight;
    private LocalDate birthDate;
    private Instant createdAt;
    private Instant updatedAt;
}
