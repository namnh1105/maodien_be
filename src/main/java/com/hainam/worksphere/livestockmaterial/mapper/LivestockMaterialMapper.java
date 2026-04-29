package com.hainam.worksphere.livestockmaterial.mapper;

import com.hainam.worksphere.livestockmaterial.domain.LivestockMaterial;
import com.hainam.worksphere.livestockmaterial.dto.response.LivestockMaterialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LivestockMaterialMapper {

    @Mapping(target = "materialType", expression = "java(livestockMaterial.getMaterialType() != null ? livestockMaterial.getMaterialType().name() : null)")
    LivestockMaterialResponse toResponse(LivestockMaterial livestockMaterial);
}
