package com.hainam.worksphere.medicine.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMedicineRequest {

    private String name;
    private String medicineType;
    private String unit;
    private String manufacturer;
    private String description;
}
