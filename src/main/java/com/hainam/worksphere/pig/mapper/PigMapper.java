package com.hainam.worksphere.pig.mapper;

import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.dto.response.PigResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PigMapper {

    @Mapping(target = "gender", expression = "java(pig.getGender() != null ? pig.getGender().name() : null)")
    @Mapping(target = "status", expression = "java(pig.getStatus() != null ? pig.getStatus().name() : null)")
    PigResponse toResponse(Pig pig);
}
