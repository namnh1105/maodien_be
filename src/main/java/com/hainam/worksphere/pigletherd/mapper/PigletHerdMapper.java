package com.hainam.worksphere.pigletherd.mapper;

import com.hainam.worksphere.pigletherd.domain.PigletHerd;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PigletHerdMapper {

    @Mapping(target = "motherId", source = "mother.id")
    @Mapping(target = "motherEarTag", source = "mother.earTag")
    @Mapping(target = "motherBreed", source = "mother.species")
    @Mapping(target = "fatherId", source = "father.id")
    @Mapping(target = "fatherEarTag", source = "father.earTag")
    @Mapping(target = "fatherBreed", source = "father.species")
    @Mapping(target = "semenCode", ignore = true)
    PigletHerdResponse toResponse(PigletHerd pigletHerd);
}
