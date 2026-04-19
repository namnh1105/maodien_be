package com.hainam.worksphere.pigletherd.repository;

import com.hainam.worksphere.pigletherd.domain.PigletHerdGrowth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PigletHerdGrowthRepository extends JpaRepository<PigletHerdGrowth, UUID> {

    @Query("SELECT g FROM PigletHerdGrowth g WHERE g.isDeleted = false")
    List<PigletHerdGrowth> findAllActive();

    @Query("SELECT g FROM PigletHerdGrowth g WHERE g.id = :id AND g.isDeleted = false")
    Optional<PigletHerdGrowth> findActiveById(@Param("id") UUID id);

    @Query("SELECT g FROM PigletHerdGrowth g WHERE g.herdId = :herdId AND g.isDeleted = false ORDER BY g.trackingDate DESC")
    List<PigletHerdGrowth> findActiveByHerdId(@Param("herdId") UUID herdId);
}
