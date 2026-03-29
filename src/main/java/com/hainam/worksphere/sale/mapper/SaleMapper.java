package com.hainam.worksphere.sale.mapper;

import com.hainam.worksphere.sale.domain.Sale;
import com.hainam.worksphere.sale.dto.response.SaleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SaleMapper {

    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.name")
    @Mapping(target = "pigId", source = "pig.id")
    @Mapping(target = "pigCode", source = "pig.pigCode")
    SaleResponse toResponse(Sale sale);
}
