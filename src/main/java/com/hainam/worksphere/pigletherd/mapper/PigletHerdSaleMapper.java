package com.hainam.worksphere.pigletherd.mapper;

import com.hainam.worksphere.pigletherd.domain.PigletHerdSale;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdSaleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PigletHerdSaleMapper {

    @Mapping(target = "herdId", source = "herd.id")
    @Mapping(target = "herdName", source = "herd.herdName")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.name")
    PigletHerdSaleResponse toResponse(PigletHerdSale sale);
}
