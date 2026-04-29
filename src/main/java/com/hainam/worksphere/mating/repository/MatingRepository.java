package com.hainam.worksphere.mating.repository;

import com.hainam.worksphere.mating.domain.Mating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MatingRepository extends JpaRepository<Mating, UUID> {

    @Query("SELECT m FROM Mating m WHERE m.isDeleted = false")
    List<Mating> findAllActive();

    @Query("SELECT m FROM Mating m WHERE m.id = :id AND m.isDeleted = false")
    Optional<Mating> findActiveById(@Param("id") UUID id);

    @Query("SELECT m FROM Mating m WHERE m.sowPigId = :sowPigId AND m.isDeleted = false ORDER BY m.matingDate DESC NULLS LAST")
    List<Mating> findActiveBySowPigId(@Param("sowPigId") UUID sowPigId);

    /** Đếm tổng số lần mang thai của lợn nái (số lần phối). */
    @Query("SELECT COUNT(m) FROM Mating m WHERE m.sowPigId = :sowPigId AND m.isDeleted = false")
    long countActiveBySowPigId(@Param("sowPigId") UUID sowPigId);
}

