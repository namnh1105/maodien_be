package com.hainam.worksphere.cullingproposal.dto.request;

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
public class CreateCullingProposalRequest {

    @NotBlank(message = "Proposal code is required")
    private String proposalCode;

    @NotNull(message = "Pig id is required")
    private UUID pigId;

    @NotBlank(message = "Proposal type is required")
    private String proposalType;

    private String reason;

    @NotNull(message = "Employee id is required")
    private UUID employeeId;

    private String status;
}
