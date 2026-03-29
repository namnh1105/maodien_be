package com.hainam.worksphere.feed.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFeedRequest {

    @Size(max = 150)
    private String name;

    @Size(max = 50)
    private String unit;
}
