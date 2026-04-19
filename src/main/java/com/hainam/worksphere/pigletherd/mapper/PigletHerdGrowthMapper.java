package com.hainam.worksphere.pigletherd.mapper;

import com.hainam.worksphere.pigletherd.domain.PigletHerdGrowth;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdGrowthResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PigletHerdGrowthMapper {

    PigletHerdGrowthResponse toResponse(PigletHerdGrowth growth);
}
