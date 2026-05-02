package com.hainam.worksphere.penpig.dto.request;

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
public class CreatePenPigRequest {

    @NotNull(message = "Mã phân chuồng không được để trống")
    private UUID penId;
    private UUID pigId;
    private UUID herdId;
    private LocalDate entryDate;
    private LocalDate exitDate;
    private String status;
}
