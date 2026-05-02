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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    FeedingRationDetail toEntity(CreateFeedingRationDetailRequest request);

    @Mapping(target = "feedId", source = "feed.id")
    @Mapping(target = "feedName", source = "feed.name")
    FeedingRationDetailResponse toResponse(FeedingRationDetail feedingRationDetail);

    @Mapping(target = "feed", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "deletedBy", ignore = true)
    void updateEntityFromRequest(UpdateFeedingRationDetailRequest request, @MappingTarget FeedingRationDetail feedingRationDetail);
}
