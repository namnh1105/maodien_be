package com.hainam.worksphere.cullingproposal.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCullingProposalBulkRequest {

    @NotBlank(message = "Pig ear tag is required")
    private String pigEarTag;

    @NotBlank(message = "Proposal type is required")
    private String proposalType;

    private String reason;
}
