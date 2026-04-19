package com.hainam.worksphere.growthtracking.mapper;

import com.hainam.worksphere.growthtracking.domain.GrowthTracking;
import com.hainam.worksphere.growthtracking.dto.response.GrowthTrackingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface GrowthTrackingMapper {

    GrowthTrackingResponse toResponse(GrowthTracking growthTracking);
}
