package com.hainam.worksphere.workschedule.dto.response;

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
public class WorkScheduleResponse {

    private UUID id;
    private String scheduleCode;
    private UUID employeeId;
    private String workName;
    private UUID areaId;
    private String shift;
    private String note;
    private String status;
    private LocalDate workDate;
    private Instant createdAt;
    private Instant updatedAt;
}
