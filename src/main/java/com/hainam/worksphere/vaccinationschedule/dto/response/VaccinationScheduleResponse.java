package com.hainam.worksphere.vaccinationschedule.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VaccinationScheduleResponse {

    private UUID id;
    private String scheduleCode;
    private UUID penId;
    private UUID employeeId;
    private Instant createdAt;
    private Instant updatedAt;
}
