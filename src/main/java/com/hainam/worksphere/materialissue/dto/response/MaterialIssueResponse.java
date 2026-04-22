package com.hainam.worksphere.materialissue.dto.response;

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
public class MaterialIssueResponse {

    private UUID id;
    private LocalDate issueDate;
    private UUID employeeId;
    private String reason;
    private Instant createdAt;
    private Instant updatedAt;
}
