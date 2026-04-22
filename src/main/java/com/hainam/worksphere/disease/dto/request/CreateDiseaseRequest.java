package com.hainam.worksphere.disease.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDiseaseRequest {
    @NotBlank(message = "Disease name is required")
    private String name;

    private String diseaseType;
    private String symptoms;
}
