package com.hainam.worksphere.medicine.dto.request;

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
public class CreateMedicineRequest {

    @NotBlank(message = "Mã thuốc không được để trống")
    @NotBlank(message = "Tên thuốc không được để trống")
    @Size(max = 200)
    private String name;

    private String medicineType;
    private String unit;
    private String manufacturer;
    private String description;
}
