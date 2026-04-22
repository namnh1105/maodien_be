package com.hainam.worksphere.vaccine.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class VaccineResponse {
    private UUID id;
    private String name;
    private String unit;
    private String treatmentDisease;
    private Instant createdAt;
    private Instant updatedAt;
}
