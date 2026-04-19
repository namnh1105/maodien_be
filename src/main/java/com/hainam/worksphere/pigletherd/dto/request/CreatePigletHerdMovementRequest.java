package com.hainam.worksphere.pigletherd.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePigletHerdMovementRequest {

    @NotNull(message = "Herd id is required")
    private UUID herdId;

    @NotNull(message = "Movement type is required")
    private String movementType;

    private UUID sourceHerdId;

    private UUID targetHerdId;

    @NotNull(message = "Movement date is required")
    private LocalDate movementDate;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    private String reason;
}
