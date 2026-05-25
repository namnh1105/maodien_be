package com.hainam.worksphere.pigletherd.dto.request;

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
public class UpdatePigletHerdStatusRequest {

    @NotNull(message = "Herd id is required")
    private UUID id;

    @NotNull(message = "Status is required")
    private com.hainam.worksphere.pigletherd.domain.PigletHerdStatus status;
}
