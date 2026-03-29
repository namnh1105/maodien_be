package com.hainam.worksphere.vaccine.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdateVaccineRequest {
    @Size(max = 150)
    private String name;
    @Size(max = 50)
    private String unit;
    private String treatmentDisease;
}
