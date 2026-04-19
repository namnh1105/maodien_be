package com.hainam.worksphere.pigletherd.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MergePigletHerdRequest {

    @NotNull(message = "Target herd id is required")
    private UUID targetHerdId;

    @NotEmpty(message = "Source herd ids are required")
    private List<UUID> sourceHerdIds;

    @NotNull(message = "Movement date is required")
    private LocalDate movementDate;

    private String reason;
}
