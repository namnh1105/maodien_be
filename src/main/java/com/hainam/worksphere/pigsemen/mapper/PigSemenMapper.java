package com.hainam.worksphere.pigsemen.mapper;

import com.hainam.worksphere.pigsemen.domain.PigSemen;
import com.hainam.worksphere.pigsemen.dto.response.PigSemenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PigSemenMapper {

    @Mapping(target = "boarPigEarTag", ignore = true)
    PigSemenResponse toResponse(PigSemen pigSemen);
}
