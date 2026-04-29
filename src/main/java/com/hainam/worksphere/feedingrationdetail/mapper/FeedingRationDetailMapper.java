package com.hainam.worksphere.feedingrationdetail.mapper;

import com.hainam.worksphere.feedingrationdetail.domain.FeedingRationDetail;
import com.hainam.worksphere.feedingrationdetail.dto.request.CreateFeedingRationDetailRequest;
import com.hainam.worksphere.feedingrationdetail.dto.request.UpdateFeedingRationDetailRequest;
import com.hainam.worksphere.feedingrationdetail.dto.response.FeedingRationDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FeedingRationDetailMapper {

    @Mapping(target = "feed", ignore = true)
    FeedingRationDetail toEntity(CreateFeedingRationDetailRequest request);

    @Mapping(target = "feedId", source = "feed.id")
    @Mapping(target = "feedName", source = "feed.name")
    FeedingRationDetailResponse toResponse(FeedingRationDetail feedingRationDetail);

    @Mapping(target = "feed", ignore = true)
    void updateEntityFromRequest(UpdateFeedingRationDetailRequest request, @MappingTarget FeedingRationDetail feedingRationDetail);
}
