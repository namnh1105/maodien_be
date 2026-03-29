package com.hainam.worksphere.warehouseimport.repository;

import com.hainam.worksphere.warehouseimport.domain.WarehouseImport;
import com.hainam.worksphere.warehouseimport.domain.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WarehouseImportRepository extends JpaRepository<WarehouseImport, UUID> {

    @Query("SELECT wi FROM WarehouseImport wi WHERE wi.isDeleted = false")
    List<WarehouseImport> findAllActive();

    @Query("SELECT wi FROM WarehouseImport wi WHERE wi.id = :id AND wi.isDeleted = false")
    Optional<WarehouseImport> findActiveById(@Param("id") UUID id);

    @Query("SELECT wi FROM WarehouseImport wi WHERE wi.warehouse.id = :warehouseId AND wi.isDeleted = false")
    List<WarehouseImport> findActiveByWarehouseId(@Param("warehouseId") UUID warehouseId);

    @Query("SELECT wi FROM WarehouseImport wi WHERE wi.itemType = :itemType AND wi.isDeleted = false")
    List<WarehouseImport> findActiveByItemType(@Param("itemType") ItemType itemType);
}
