package com.hainam.worksphere.pen.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PenResponse {
    private UUID id;
    private String name;
    private Double area;
    private UUID areaId;
    private String penType;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
