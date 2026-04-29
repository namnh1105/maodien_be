package com.hainam.worksphere.medicine.mapper;

import com.hainam.worksphere.medicine.domain.Medicine;
import com.hainam.worksphere.medicine.dto.response.MedicineResponse;

@Deprecated
public interface MedicineMapper {

    MedicineResponse toResponse(Medicine medicine);
}
