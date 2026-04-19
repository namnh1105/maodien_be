package com.hainam.worksphere.pen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PenPigSummaryResponse {
    private UUID pigId;
    private String earTag;
    private Double currentWeight;
    private String type;
    private String status;
}
