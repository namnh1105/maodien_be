package com.hainam.worksphere.pigsemen.dto.response;

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
public class PigSemenResponse {

    private UUID id;
    private String code;
    private UUID boarPigId;
    private String boarPigEarTag;
    private String boarBreed;
    private LocalDate collectionDate;
    private Double volume;
    private Double motility;
    private String quality;
    private String status;
    private String note;
    private Instant createdAt;
    private Instant updatedAt;
}
