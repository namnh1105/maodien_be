package com.hainam.worksphere.cullingproposal.dto.response;

import com.hainam.worksphere.cullingproposal.domain.CullingProposalStatus;
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
    private UUID pigId;
    private String pigEarTag;
    private String proposalType;
    private String reason;
    private UUID employeeId;
    private String employeeName;
    private CullingProposalStatus status;
    private Instant createdAt;
    private Instant updatedAt;
}
