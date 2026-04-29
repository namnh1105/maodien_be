package com.hainam.worksphere.feedingrationdetail.dto.request;

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
public class CreateFeedingRationDetailRequest {

    @NotNull(message = "Ration id is required")
    private UUID rationId;

    @NotNull(message = "Feed id is required")
    private UUID feedId;

    private Double totalFeedAmount;
}
