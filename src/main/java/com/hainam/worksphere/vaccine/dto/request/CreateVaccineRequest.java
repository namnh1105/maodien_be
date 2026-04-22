package com.hainam.worksphere.vaccine.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateVaccineRequest {
    @NotBlank(message = "Mã thuốc không được để trống") @Size(max = 30)
    @NotBlank(message = "Tên thuốc không được để trống") @Size(max = 150)
    private String name;
    @Size(max = 50)
    private String unit;
    private String treatmentDisease;
}
