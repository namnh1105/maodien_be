package com.hainam.worksphere.feed.mapper;

import com.hainam.worksphere.feed.domain.Feed;
import com.hainam.worksphere.feed.dto.response.FeedResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface FeedMapper {

    FeedResponse toResponse(Feed feed);
}
