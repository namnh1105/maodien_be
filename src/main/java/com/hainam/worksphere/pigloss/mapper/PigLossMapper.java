package com.hainam.worksphere.pigloss.mapper;

import com.hainam.worksphere.pigloss.domain.PigLoss;
import com.hainam.worksphere.pigloss.dto.response.PigLossResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PigLossMapper {

    @Mapping(target = "pigEarTag", ignore = true)
    PigLossResponse toResponse(PigLoss pigLoss);
}
