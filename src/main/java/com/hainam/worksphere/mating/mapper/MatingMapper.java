package com.hainam.worksphere.mating.mapper;

import com.hainam.worksphere.mating.domain.Mating;
import com.hainam.worksphere.mating.dto.response.MatingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MatingMapper {

    @Mapping(target = "sowPigEarTag", ignore = true)
    @Mapping(target = "sowBreed", ignore = true)
    @Mapping(target = "semenCode", ignore = true)
    @Mapping(target = "boarBreed", ignore = true)
    MatingResponse toResponse(Mating mating);
}
