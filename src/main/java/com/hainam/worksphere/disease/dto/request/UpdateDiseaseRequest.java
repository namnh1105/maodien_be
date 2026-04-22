package com.hainam.worksphere.disease.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDiseaseRequest {
    private String name;
    private String diseaseType;
    private String symptoms;
}
