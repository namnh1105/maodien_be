package com.hainam.worksphere.feed.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedResponse {

    private UUID id;
    private String name;
    private String unit;
    private Instant createdAt;
    private Instant updatedAt;
}
