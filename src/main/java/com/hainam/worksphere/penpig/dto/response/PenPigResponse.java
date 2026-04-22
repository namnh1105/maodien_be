package com.hainam.worksphere.penpig.dto.response;

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
public class PenPigResponse {

    private UUID id;
    private UUID penId;
    private UUID pigId;
    private LocalDate entryDate;
    private LocalDate exitDate;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
