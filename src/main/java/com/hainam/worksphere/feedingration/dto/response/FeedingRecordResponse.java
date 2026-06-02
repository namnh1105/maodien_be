package com.hainam.worksphere.feedingration.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedingRecordResponse {
    private UUID rationId;
    private UUID detailId;
    private UUID penId;
    private UUID herdId;
    private UUID feedId;
    private String feedName;
    private LocalDate feedingDate;
    private Integer feedingNumber;
    private Double feedAmount;
    private String note;
}
