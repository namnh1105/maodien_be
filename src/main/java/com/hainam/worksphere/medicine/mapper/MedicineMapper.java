package com.hainam.worksphere.medicine.mapper;

import com.hainam.worksphere.medicine.domain.Medicine;
import com.hainam.worksphere.medicine.dto.response.MedicineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MedicineMapper {

    MedicineResponse toResponse(Medicine medicine);
}
