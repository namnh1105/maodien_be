package com.hainam.worksphere.mating.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMatingStatusRequest {

    @NotNull(message = "Mating id is required")
    private UUID id;

    @NotBlank(message = "Status is required")
    private String status;
}
