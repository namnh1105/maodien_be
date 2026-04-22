package com.hainam.worksphere.pigletherd.mapper;

import com.hainam.worksphere.pigletherd.domain.PigletHerd;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PigletHerdMapper {

    @Mapping(target = "motherId", source = "mother.id")
    @Mapping(target = "fatherId", source = "father.id")
    PigletHerdResponse toResponse(PigletHerd pigletHerd);
}
