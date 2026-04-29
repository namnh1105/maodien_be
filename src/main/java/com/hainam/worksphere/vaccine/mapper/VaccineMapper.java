package com.hainam.worksphere.vaccine.mapper;

import com.hainam.worksphere.vaccine.domain.Vaccine;
import com.hainam.worksphere.vaccine.dto.response.VaccineResponse;

@Deprecated
public interface VaccineMapper {

    VaccineResponse toResponse(Vaccine vaccine);
}
