package com.hainam.worksphere.vaccinationschedule.mapper;

import com.hainam.worksphere.vaccinationschedule.domain.VaccinationSchedule;
import com.hainam.worksphere.vaccinationschedule.dto.request.CreateVaccinationScheduleRequest;
import com.hainam.worksphere.vaccinationschedule.dto.request.UpdateVaccinationScheduleRequest;
import com.hainam.worksphere.vaccinationschedule.dto.response.VaccinationScheduleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface VaccinationScheduleMapper {

    VaccinationSchedule toEntity(CreateVaccinationScheduleRequest request);

    VaccinationScheduleResponse toResponse(VaccinationSchedule vaccinationSchedule);

    void updateEntityFromRequest(UpdateVaccinationScheduleRequest request, @MappingTarget VaccinationSchedule vaccinationSchedule);
}
