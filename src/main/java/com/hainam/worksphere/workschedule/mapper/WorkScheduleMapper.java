package com.hainam.worksphere.workschedule.mapper;

import com.hainam.worksphere.workschedule.domain.WorkSchedule;
import com.hainam.worksphere.workschedule.dto.request.CreateWorkScheduleRequest;
import com.hainam.worksphere.workschedule.dto.request.UpdateWorkScheduleRequest;
import com.hainam.worksphere.workschedule.dto.response.WorkScheduleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface WorkScheduleMapper {

    WorkSchedule toEntity(CreateWorkScheduleRequest request);

    WorkScheduleResponse toResponse(WorkSchedule workSchedule);

    void updateEntityFromRequest(UpdateWorkScheduleRequest request, @MappingTarget WorkSchedule workSchedule);
}
