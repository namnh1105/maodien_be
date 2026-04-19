package com.hainam.worksphere.pig.dto.response;

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
public class PigGrowthItemResponse {
    private UUID id;
    private LocalDate trackingDate;
    private Double litterLength;
    private Double chestGirth;
    private Double weight;
    private Double adg;
    private Double fcr;
}
