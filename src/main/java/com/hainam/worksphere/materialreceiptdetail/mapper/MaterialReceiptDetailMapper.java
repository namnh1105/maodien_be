package com.hainam.worksphere.materialreceiptdetail.mapper;

import com.hainam.worksphere.materialreceiptdetail.domain.MaterialReceiptDetail;
import com.hainam.worksphere.materialreceiptdetail.dto.request.CreateMaterialReceiptDetailRequest;
import com.hainam.worksphere.materialreceiptdetail.dto.request.UpdateMaterialReceiptDetailRequest;
import com.hainam.worksphere.materialreceiptdetail.dto.response.MaterialReceiptDetailResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface MaterialReceiptDetailMapper {

    MaterialReceiptDetail toEntity(CreateMaterialReceiptDetailRequest request);

    MaterialReceiptDetailResponse toResponse(MaterialReceiptDetail materialReceiptDetail);

    void updateEntityFromRequest(UpdateMaterialReceiptDetailRequest request, @MappingTarget MaterialReceiptDetail materialReceiptDetail);
}
