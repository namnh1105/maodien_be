package com.hainam.worksphere.pen.mapper;

import com.hainam.worksphere.pen.domain.Pen;
import com.hainam.worksphere.pen.dto.response.PenResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PenMapper {

    @Mapping(target = "penType", expression = "java(pen.getPenType() != null ? pen.getPenType().name() : null)")
    @Mapping(target = "status", expression = "java(pen.getStatus() != null ? pen.getStatus().name() : null)")
    PenResponse toResponse(Pen pen);
}
