package com.hainam.worksphere.livestockmaterial.dto.response;

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
public class LivestockMaterialResponse {

    private UUID id;
    private String name;
    private String unit;
    private String materialType;
    private Double quantity;
    private String description;
    private Instant createdAt;
    private Instant updatedAt;
}
