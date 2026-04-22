package com.hainam.worksphere.cullingproposal.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCullingProposalRequest {
    private UUID pigId;
    private String proposalType;
    private String reason;
    private UUID employeeId;
    private String status;
}
