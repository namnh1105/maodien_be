package com.hainam.worksphere.livestockmaterial.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLivestockMaterialRequest {

    @Size(max = 150)
    private String name;

    @Size(max = 50)
    private String unit;

    @Size(max = 50)
    private String materialType;

    @Size(max = 1000)
    private String description;
}
