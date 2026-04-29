package com.hainam.worksphere.feedingrationdetail.dto.response;

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
public class FeedingRationDetailResponse {

    private UUID id;
    private UUID rationId;
    private UUID feedId;
    private String feedName;
    private Double totalFeedAmount;
    private Instant createdAt;
    private Instant updatedAt;
}
