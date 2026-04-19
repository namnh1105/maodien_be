package com.hainam.worksphere.pig.dto.response;

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
public class PigVaccinationItemResponse {
    private UUID id;
    private String vaccineName;
    private LocalDate vaccinationDate;
    private String dosage;
    private String note;
}
