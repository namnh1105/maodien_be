package com.hainam.worksphere.reproductioncycle.mapper;

import com.hainam.worksphere.reproductioncycle.domain.ReproductionCycle;
import com.hainam.worksphere.reproductioncycle.dto.response.ReproductionCycleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReproductionCycleMapper {

    ReproductionCycleResponse toResponse(ReproductionCycle reproductionCycle);
}
