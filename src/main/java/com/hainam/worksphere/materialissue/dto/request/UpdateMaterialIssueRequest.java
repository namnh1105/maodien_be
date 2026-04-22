package com.hainam.worksphere.materialissue.dto.request;

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
public class UpdateMaterialIssueRequest {
    private LocalDate issueDate;
    private UUID employeeId;
    private String reason;
}
