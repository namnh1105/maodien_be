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

    @Query("SELECT m FROM Mating m WHERE m.sowPigId = :sowPigId AND m.isDeleted = false")
    List<Mating> findActiveBySowPigId(@Param("sowPigId") UUID sowPigId);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Mating m WHERE m.matingCode = :code AND m.isDeleted = false")
    boolean existsActiveByMatingCode(@Param("code") String code);
}
