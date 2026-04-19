package com.hainam.worksphere.growthtracking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrowthTrackingResponse {

    private UUID id;
    private String trackingCode;
    private UUID pigId;
    private LocalDate trackingDate;
    private Double litterLength;
    private Double chestGirth;
    private Double weight;
    private Double growthRate;
    private Double adg;
    private Double fcr;
    private String note;
    private Instant createdAt;
    private Instant updatedAt;
}
