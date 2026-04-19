package com.hainam.worksphere.cullingproposal.mapper;

import com.hainam.worksphere.cullingproposal.domain.CullingProposal;
import com.hainam.worksphere.cullingproposal.dto.request.CreateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.request.UpdateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.response.CullingProposalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CullingProposalMapper {

    CullingProposal toEntity(CreateCullingProposalRequest request);

    CullingProposalResponse toResponse(CullingProposal cullingProposal);

    void updateEntityFromRequest(UpdateCullingProposalRequest request, @MappingTarget CullingProposal cullingProposal);
}
