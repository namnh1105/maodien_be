package com.hainam.worksphere.mating.mapper;

import com.hainam.worksphere.mating.domain.Mating;
import com.hainam.worksphere.mating.dto.response.MatingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MatingMapper {

    MatingResponse toResponse(Mating mating);
}
