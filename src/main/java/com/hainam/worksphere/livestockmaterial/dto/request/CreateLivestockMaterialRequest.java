package com.hainam.worksphere.livestockmaterial.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLivestockMaterialRequest {

    @NotBlank(message = "Material code is required")
    @NotBlank(message = "Material name is required")
    @Size(max = 150)
    private String name;

    @Size(max = 50)
    private String unit;
}
