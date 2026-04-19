package com.hainam.worksphere.disease.mapper;

import com.hainam.worksphere.disease.domain.Disease;
import com.hainam.worksphere.disease.dto.request.CreateDiseaseRequest;
import com.hainam.worksphere.disease.dto.request.UpdateDiseaseRequest;
import com.hainam.worksphere.disease.dto.response.DiseaseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DiseaseMapper {

    Disease toEntity(CreateDiseaseRequest request);

    DiseaseResponse toResponse(Disease disease);

    void updateEntityFromRequest(UpdateDiseaseRequest request, @MappingTarget Disease disease);
}
