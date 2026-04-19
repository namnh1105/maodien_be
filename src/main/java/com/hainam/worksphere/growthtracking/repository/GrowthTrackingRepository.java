package com.hainam.worksphere.growthtracking.repository;

import com.hainam.worksphere.growthtracking.domain.GrowthTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface GrowthTrackingRepository extends JpaRepository<GrowthTracking, UUID> {

    @Query("SELECT gt FROM GrowthTracking gt WHERE gt.isDeleted = false")
    List<GrowthTracking> findAllActive();

    @Query("SELECT gt FROM GrowthTracking gt WHERE gt.id = :id AND gt.isDeleted = false")
    Optional<GrowthTracking> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(gt) > 0 THEN true ELSE false END FROM GrowthTracking gt WHERE gt.trackingCode = :code AND gt.isDeleted = false")
    boolean existsActiveByTrackingCode(@Param("code") String code);
}
