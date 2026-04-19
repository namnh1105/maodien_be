package com.hainam.worksphere.feedingration.mapper;

import com.hainam.worksphere.feedingration.domain.FeedingRation;
import com.hainam.worksphere.feedingration.dto.request.CreateFeedingRationRequest;
import com.hainam.worksphere.feedingration.dto.request.UpdateFeedingRationRequest;
import com.hainam.worksphere.feedingration.dto.response.FeedingRationResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FeedingRationMapper {

    FeedingRation toEntity(CreateFeedingRationRequest request);

    FeedingRationResponse toResponse(FeedingRation feedingRation);

    void updateEntityFromRequest(UpdateFeedingRationRequest request, @MappingTarget FeedingRation feedingRation);
}
