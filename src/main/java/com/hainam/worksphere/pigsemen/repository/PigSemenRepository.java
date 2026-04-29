package com.hainam.worksphere.pigsemen.repository;

import com.hainam.worksphere.pigsemen.domain.PigSemen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PigSemenRepository extends JpaRepository<PigSemen, UUID> {

    @Query("SELECT ps FROM PigSemen ps WHERE ps.isDeleted = false")
    List<PigSemen> findAllActive();

    @Query("SELECT ps FROM PigSemen ps WHERE ps.id = :id AND ps.isDeleted = false")
    Optional<PigSemen> findActiveById(@Param("id") UUID id);

    @Query("SELECT ps FROM PigSemen ps WHERE ps.boarPigId = :boarPigId AND ps.isDeleted = false")
    List<PigSemen> findActiveByBoarPigId(@Param("boarPigId") UUID boarPigId);
}
