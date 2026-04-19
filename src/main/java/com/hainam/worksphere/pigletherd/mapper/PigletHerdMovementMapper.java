package com.hainam.worksphere.pigletherd.mapper;

import com.hainam.worksphere.pigletherd.domain.PigletHerdMovement;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdMovementResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PigletHerdMovementMapper {

    @Mapping(target = "movementType", expression = "java(movement.getMovementType() != null ? movement.getMovementType().name() : null)")
    PigletHerdMovementResponse toResponse(PigletHerdMovement movement);
}
