package com.hainam.worksphere.vaccination.dto.response;

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
public class VaccinationResponse {

    private UUID id;
    private UUID pigId;
    private UUID vaccineId;
    private String vaccineName;
    private LocalDate vaccinationDate;
    private String dosage;
    private UUID employeeId;
    private String employeeName;
    private String note;
    private Instant createdAt;
    private Instant updatedAt;
}
