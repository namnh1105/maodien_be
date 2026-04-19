package com.hainam.worksphere.pigletherd.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PigletHerdMovementResponse {

    private UUID id;
    private UUID herdId;
    private String movementType;
    private UUID sourceHerdId;
    private UUID targetHerdId;
    private LocalDate movementDate;
    private Integer quantity;
    private String reason;
    private Instant createdAt;
    private Instant updatedAt;
}
