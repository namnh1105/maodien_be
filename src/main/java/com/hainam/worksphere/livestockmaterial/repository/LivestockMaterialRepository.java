package com.hainam.worksphere.livestockmaterial.repository;

import com.hainam.worksphere.livestockmaterial.domain.LivestockMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LivestockMaterialRepository extends JpaRepository<LivestockMaterial, UUID> {

    @Query("SELECT m FROM LivestockMaterial m WHERE m.isDeleted = false")
    List<LivestockMaterial> findAllActive();

    @Query("SELECT m FROM LivestockMaterial m WHERE m.id = :id AND m.isDeleted = false")
    Optional<LivestockMaterial> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM LivestockMaterial m WHERE m.name = :name AND m.isDeleted = false")
    boolean existsActiveByName(@Param("name") String name);
}
