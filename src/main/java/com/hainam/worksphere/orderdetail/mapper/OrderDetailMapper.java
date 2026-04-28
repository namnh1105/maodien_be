package com.hainam.worksphere.orderdetail.mapper;

import com.hainam.worksphere.orderdetail.domain.OrderDetail;
import com.hainam.worksphere.orderdetail.dto.request.CreateOrderDetailRequest;
import com.hainam.worksphere.orderdetail.dto.request.UpdateOrderDetailRequest;
import com.hainam.worksphere.orderdetail.dto.response.OrderDetailResponse;
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
public interface OrderDetailMapper {

    OrderDetail toEntity(CreateOrderDetailRequest request);

    @Mapping(target = "pigEarTag", ignore = true)
    OrderDetailResponse toResponse(OrderDetail orderDetail);

    void updateEntityFromRequest(UpdateOrderDetailRequest request, @MappingTarget OrderDetail orderDetail);
}
