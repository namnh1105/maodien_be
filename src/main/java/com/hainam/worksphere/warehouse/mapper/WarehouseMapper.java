package com.hainam.worksphere.warehouse.mapper;

import com.hainam.worksphere.warehouse.domain.Warehouse;
import com.hainam.worksphere.warehouse.dto.response.WarehouseResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {

    @Mapping(target = "warehouseType", expression = "java(warehouse.getWarehouseType() != null ? warehouse.getWarehouseType().name() : null)")
    WarehouseResponse toResponse(Warehouse warehouse);
}
