package com.hainam.worksphere.cullingproposal.mapper;

import com.hainam.worksphere.cullingproposal.domain.CullingProposal;
import com.hainam.worksphere.cullingproposal.dto.request.CreateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.request.UpdateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.response.CullingProposalResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CullingProposalMapper {

    CullingProposal toEntity(CreateCullingProposalRequest request);

    @Mapping(target = "pigEarTag", ignore = true)
    CullingProposalResponse toResponse(CullingProposal cullingProposal);

    void updateEntityFromRequest(UpdateCullingProposalRequest request, @MappingTarget CullingProposal cullingProposal);
}
