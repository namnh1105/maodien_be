package com.hainam.worksphere.warehouseimport.mapper;

import com.hainam.worksphere.warehouseimport.domain.WarehouseImport;
import com.hainam.worksphere.warehouseimport.dto.response.WarehouseImportResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseImportMapper {

    @Mapping(target = "warehouseId", source = "warehouse.id")
    @Mapping(target = "warehouseName", source = "warehouse.name")
    @Mapping(target = "itemType", expression = "java(warehouseImport.getItemType() != null ? warehouseImport.getItemType().name() : null)")
    @Mapping(target = "supplierId", source = "supplier.id")
    @Mapping(target = "supplierName", source = "supplier.name")
    WarehouseImportResponse toResponse(WarehouseImport warehouseImport);
}
