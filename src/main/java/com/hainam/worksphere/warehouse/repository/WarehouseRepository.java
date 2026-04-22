package com.hainam.worksphere.warehouse.repository;

import com.hainam.worksphere.warehouse.domain.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, UUID> {

    @Query("SELECT w FROM Warehouse w WHERE w.isDeleted = false")
    List<Warehouse> findAllActive();

    @Query("SELECT w FROM Warehouse w WHERE w.id = :id AND w.isDeleted = false")
    Optional<Warehouse> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(w) > 0 THEN true ELSE false END FROM Warehouse w WHERE w.name = :name AND w.isDeleted = false")
    boolean existsActiveByName(@Param("name") String name);
}
