package com.hainam.worksphere.pigletherd.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePigletHerdGrowthRequest {

    private LocalDate trackingDate;

    private Double averageWeight;

    private String note;
}
