package com.hainam.worksphere.livestockmaterial.mapper;

import com.hainam.worksphere.livestockmaterial.domain.LivestockMaterial;
import com.hainam.worksphere.livestockmaterial.dto.response.LivestockMaterialResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LivestockMaterialMapper {

    LivestockMaterialResponse toResponse(LivestockMaterial livestockMaterial);
}
