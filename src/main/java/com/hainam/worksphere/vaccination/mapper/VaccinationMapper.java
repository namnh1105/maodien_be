package com.hainam.worksphere.vaccination.mapper;

import com.hainam.worksphere.vaccination.domain.Vaccination;
import com.hainam.worksphere.vaccination.dto.response.VaccinationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface VaccinationMapper {

    @Mapping(target = "pigId", source = "pig.id")
    @Mapping(target = "pigCode", source = "pig.pigCode")
    @Mapping(target = "vaccineId", source = "vaccine.id")
    @Mapping(target = "vaccineCode", source = "vaccine.vaccineCode")
    @Mapping(target = "vaccineName", source = "vaccine.name")
    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeName", source = "employee.fullName")
    VaccinationResponse toResponse(Vaccination vaccination);
}
