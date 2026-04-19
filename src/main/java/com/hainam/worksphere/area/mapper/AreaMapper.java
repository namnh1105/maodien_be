package com.hainam.worksphere.area.mapper;

import com.hainam.worksphere.area.domain.Area;
import com.hainam.worksphere.area.dto.request.CreateAreaRequest;
import com.hainam.worksphere.area.dto.request.UpdateAreaRequest;
import com.hainam.worksphere.area.dto.response.AreaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface AreaMapper {

    Area toEntity(CreateAreaRequest request);

    AreaResponse toResponse(Area area);

    void updateEntityFromRequest(UpdateAreaRequest request, @MappingTarget Area area);
}
