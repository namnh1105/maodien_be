package com.hainam.worksphere.order.mapper;

import com.hainam.worksphere.order.domain.Order;
import com.hainam.worksphere.order.dto.request.CreateOrderRequest;
import com.hainam.worksphere.order.dto.request.UpdateOrderRequest;
import com.hainam.worksphere.order.dto.response.OrderResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface OrderMapper {

    Order toEntity(CreateOrderRequest request);

    OrderResponse toResponse(Order order);

    void updateEntityFromRequest(UpdateOrderRequest request, @MappingTarget Order order);
}
