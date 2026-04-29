package com.hainam.worksphere.feed.mapper;

import com.hainam.worksphere.feed.domain.Feed;
import com.hainam.worksphere.feed.dto.response.FeedResponse;

@Deprecated
public interface FeedMapper {

    FeedResponse toResponse(Feed feed);
}
