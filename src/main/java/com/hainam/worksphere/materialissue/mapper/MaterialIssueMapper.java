package com.hainam.worksphere.materialissue.mapper;

import com.hainam.worksphere.materialissue.domain.MaterialIssue;
import com.hainam.worksphere.materialissue.dto.request.CreateMaterialIssueRequest;
import com.hainam.worksphere.materialissue.dto.request.UpdateMaterialIssueRequest;
import com.hainam.worksphere.materialissue.dto.response.MaterialIssueResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MaterialIssueMapper {

    MaterialIssue toEntity(CreateMaterialIssueRequest request);

    MaterialIssueResponse toResponse(MaterialIssue materialIssue);

    void updateEntityFromRequest(UpdateMaterialIssueRequest request, @MappingTarget MaterialIssue materialIssue);
}
