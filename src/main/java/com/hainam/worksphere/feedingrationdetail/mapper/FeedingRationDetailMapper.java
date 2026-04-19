package com.hainam.worksphere.feedingrationdetail.mapper;

import com.hainam.worksphere.feedingrationdetail.domain.FeedingRationDetail;
import com.hainam.worksphere.feedingrationdetail.dto.request.CreateFeedingRationDetailRequest;
import com.hainam.worksphere.feedingrationdetail.dto.request.UpdateFeedingRationDetailRequest;
import com.hainam.worksphere.feedingrationdetail.dto.response.FeedingRationDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface FeedingRationDetailMapper {

    FeedingRationDetail toEntity(CreateFeedingRationDetailRequest request);

    FeedingRationDetailResponse toResponse(FeedingRationDetail feedingRationDetail);

    void updateEntityFromRequest(UpdateFeedingRationDetailRequest request, @MappingTarget FeedingRationDetail feedingRationDetail);
}
