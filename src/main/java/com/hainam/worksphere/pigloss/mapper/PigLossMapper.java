package com.hainam.worksphere.pigloss.mapper;

import com.hainam.worksphere.pigloss.domain.PigLoss;
import com.hainam.worksphere.pigloss.dto.response.PigLossResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PigLossMapper {

    PigLossResponse toResponse(PigLoss pigLoss);
}
