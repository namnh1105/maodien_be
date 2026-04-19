package com.hainam.worksphere.materialissuedetail.mapper;

import com.hainam.worksphere.materialissuedetail.domain.MaterialIssueDetail;
import com.hainam.worksphere.materialissuedetail.dto.request.CreateMaterialIssueDetailRequest;
import com.hainam.worksphere.materialissuedetail.dto.request.UpdateMaterialIssueDetailRequest;
import com.hainam.worksphere.materialissuedetail.dto.response.MaterialIssueDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MaterialIssueDetailMapper {

    MaterialIssueDetail toEntity(CreateMaterialIssueDetailRequest request);

    MaterialIssueDetailResponse toResponse(MaterialIssueDetail materialIssueDetail);

    void updateEntityFromRequest(UpdateMaterialIssueDetailRequest request, @MappingTarget MaterialIssueDetail materialIssueDetail);
}
