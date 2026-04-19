package com.hainam.worksphere.penpig.mapper;

import com.hainam.worksphere.penpig.domain.PenPig;
import com.hainam.worksphere.penpig.dto.response.PenPigResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PenPigMapper {

    PenPigResponse toResponse(PenPig penPig);
}
