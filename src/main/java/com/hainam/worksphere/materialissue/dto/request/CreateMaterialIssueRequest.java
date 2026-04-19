package com.hainam.worksphere.materialissue.dto.request;

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
public class CreateMaterialIssueRequest {

    @NotBlank(message = "Issue code is required")
    private String issueCode;

    @NotNull(message = "Issue date is required")
    private LocalDate issueDate;

    @NotNull(message = "Employee id is required")
    private UUID employeeId;

    private String reason;
}
