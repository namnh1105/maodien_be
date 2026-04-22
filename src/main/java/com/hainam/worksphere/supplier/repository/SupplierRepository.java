package com.hainam.worksphere.supplier.repository;

import com.hainam.worksphere.supplier.domain.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, UUID> {

    @Query("SELECT s FROM Supplier s WHERE s.isDeleted = false")
    List<Supplier> findAllActive();

    @Query("SELECT s FROM Supplier s WHERE s.id = :id AND s.isDeleted = false")
    Optional<Supplier> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END FROM Supplier s WHERE s.name = :name AND s.isDeleted = false")
    boolean existsActiveByName(@Param("name") String name);
}
