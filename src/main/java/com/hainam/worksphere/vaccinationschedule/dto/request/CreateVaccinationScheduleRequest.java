package com.hainam.worksphere.vaccinationschedule.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVaccinationScheduleRequest {

    @NotBlank(message = "Schedule code is required")
    private String scheduleCode;

    @NotNull(message = "Pen id is required")
    private UUID penId;

    @NotNull(message = "Employee id is required")
    private UUID employeeId;
}
