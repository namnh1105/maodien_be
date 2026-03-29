package com.hainam.worksphere.vaccine.mapper;

import com.hainam.worksphere.vaccine.domain.Vaccine;
import com.hainam.worksphere.vaccine.dto.response.VaccineResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VaccineMapper {

    VaccineResponse toResponse(Vaccine vaccine);
}
