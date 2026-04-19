package com.hainam.worksphere.penpig.dto.request;

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
public class UpdatePenPigRequest {

    private UUID penId;
    private UUID pigId;
    private LocalDate entryDate;
    private LocalDate exitDate;
    private String status;
}
