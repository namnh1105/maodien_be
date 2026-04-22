package com.hainam.worksphere.pigletherd.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class SplitPigletHerdRequest {

    @NotNull(message = "Source herd id is required")
    private UUID sourceHerdId;
    @NotNull(message = "Litter number is required")
    private Integer litterNumber;

    @NotNull(message = "Quantity is required")
    private Integer quantity;

    @NotNull(message = "Movement date is required")
    private LocalDate movementDate;

    private String reason;
}
