package com.hainam.worksphere.pig.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePigRequest {

    @Size(max = 50)
    private String earTag;

    private Double birthWeight;

    private LocalDate birthDate;

    private String type;

    @Size(max = 255)
    private String origin;

    @Size(max = 100)
    private String species;

    private Integer nippleCount;

    private LocalDate herdEntryDate;

    private String status;
}
