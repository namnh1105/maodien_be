package com.hainam.worksphere.pigletherd.dto.request;

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
public class UpdatePigletHerdMovementRequest {

    private String movementType;

    private UUID sourceHerdId;

    private UUID targetHerdId;

    private LocalDate movementDate;

    private Integer quantity;

    private String reason;
}
