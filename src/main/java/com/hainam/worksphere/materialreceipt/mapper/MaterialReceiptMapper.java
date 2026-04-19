package com.hainam.worksphere.materialreceipt.mapper;

import com.hainam.worksphere.materialreceipt.domain.MaterialReceipt;
import com.hainam.worksphere.materialreceipt.dto.request.CreateMaterialReceiptRequest;
import com.hainam.worksphere.materialreceipt.dto.request.UpdateMaterialReceiptRequest;
import com.hainam.worksphere.materialreceipt.dto.response.MaterialReceiptResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MaterialReceiptMapper {

    MaterialReceipt toEntity(CreateMaterialReceiptRequest request);

    MaterialReceiptResponse toResponse(MaterialReceipt materialReceipt);

    void updateEntityFromRequest(UpdateMaterialReceiptRequest request, @MappingTarget MaterialReceipt materialReceipt);
}
