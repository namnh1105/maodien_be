package com.hainam.worksphere.vaccination.dto.request;

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
public class CreateVaccinationRequest {

    @NotNull(message = "Pig id is required")
    private UUID pigId;

    @NotNull(message = "Vaccine id is required")
    private UUID vaccineId;

    @NotNull(message = "Vaccination date is required")
    private LocalDate vaccinationDate;

    private String dosage;

    private UUID employeeId;

    private String note;
}
