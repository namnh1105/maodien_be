package com.hainam.worksphere.area.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAreaRequest {

    private String areaCode;
    private String name;
    private String description;
}
