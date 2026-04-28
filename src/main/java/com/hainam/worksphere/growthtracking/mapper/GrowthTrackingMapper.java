package com.hainam.worksphere.growthtracking.mapper;

import com.hainam.worksphere.growthtracking.domain.GrowthTracking;
import com.hainam.worksphere.growthtracking.dto.response.GrowthTrackingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GrowthTrackingMapper {

    @Mapping(target = "pigEarTag", ignore = true)
    GrowthTrackingResponse toResponse(GrowthTracking growthTracking);
}
