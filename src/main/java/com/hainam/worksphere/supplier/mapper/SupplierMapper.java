package com.hainam.worksphere.supplier.mapper;

import com.hainam.worksphere.supplier.domain.Supplier;
import com.hainam.worksphere.supplier.dto.response.SupplierResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SupplierMapper {

    SupplierResponse toResponse(Supplier supplier);
}
