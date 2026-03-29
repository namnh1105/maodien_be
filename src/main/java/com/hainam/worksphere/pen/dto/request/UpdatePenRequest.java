package com.hainam.worksphere.pen.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class UpdatePenRequest {
    @Size(max = 100)
    private String name;
    private Double length;
    private Double width;
    private String penType;
    private String status;
}
