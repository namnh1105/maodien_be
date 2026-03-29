package com.hainam.worksphere.vaccination.dto.request;

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
public class UpdateVaccinationRequest {

    private UUID pigId;
    private UUID vaccineId;
    private LocalDate vaccinationDate;
    private String dosage;
    private UUID employeeId;
    private String note;
}
