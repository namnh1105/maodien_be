package com.hainam.worksphere.disease.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiseaseResponse {

    private UUID id;
    private String diseaseCode;
    private String name;
    private String diseaseType;
    private String symptoms;
    private Instant createdAt;
    private Instant updatedAt;
}
