package com.hainam.worksphere.workschedule.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class CreateWorkScheduleRequest {
    @NotNull(message = "Employee id is required")
    private UUID employeeId;

    @NotBlank(message = "Work name is required")
    private String workName;

    private UUID areaId;
    private String shift;
    private String note;
    private String status;

    @NotNull(message = "Work date is required")
    private LocalDate workDate;
}
