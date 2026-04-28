package com.hainam.worksphere.diseasehistory.mapper;

import com.hainam.worksphere.diseasehistory.domain.DiseaseHistory;
import com.hainam.worksphere.diseasehistory.dto.response.DiseaseHistoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DiseaseHistoryMapper {

    @Mapping(target = "pigEarTag", ignore = true)
    DiseaseHistoryResponse toResponse(DiseaseHistory diseaseHistory);
}
