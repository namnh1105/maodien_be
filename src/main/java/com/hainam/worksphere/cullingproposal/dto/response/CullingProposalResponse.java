package com.hainam.worksphere.cullingproposal.dto.response;

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
public class CullingProposalResponse {

    private UUID id;
    private String proposalCode;
    private UUID pigId;
    private String proposalType;
    private String reason;
    private UUID employeeId;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
