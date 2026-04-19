package com.hainam.worksphere.diseasehistory.mapper;

import com.hainam.worksphere.diseasehistory.domain.DiseaseHistory;
import com.hainam.worksphere.diseasehistory.dto.response.DiseaseHistoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DiseaseHistoryMapper {

    DiseaseHistoryResponse toResponse(DiseaseHistory diseaseHistory);
}
