package com.hainam.worksphere.pigletherd.repository;

import com.hainam.worksphere.pigletherd.domain.PigletHerdMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PigletHerdMovementRepository extends JpaRepository<PigletHerdMovement, UUID> {

    @Query("SELECT m FROM PigletHerdMovement m WHERE m.isDeleted = false")
    List<PigletHerdMovement> findAllActive();

    @Query("SELECT m FROM PigletHerdMovement m WHERE m.id = :id AND m.isDeleted = false")
    Optional<PigletHerdMovement> findActiveById(@Param("id") UUID id);

    @Query("""
            SELECT m FROM PigletHerdMovement m
            WHERE (m.herdId = :herdId OR m.sourceHerdId = :herdId OR m.targetHerdId = :herdId)
              AND m.isDeleted = false
            ORDER BY m.movementDate DESC, m.createdAt DESC
            """)
    List<PigletHerdMovement> findActiveByHerdId(@Param("herdId") UUID herdId);
}
