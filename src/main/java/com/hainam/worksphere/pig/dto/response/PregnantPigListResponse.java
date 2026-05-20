package com.hainam.worksphere.pig.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PregnantPigListResponse {
    private long count;
    private List<PregnantPigResponse> items;
}
