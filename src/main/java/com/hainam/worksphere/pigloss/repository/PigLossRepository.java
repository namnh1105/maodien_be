package com.hainam.worksphere.pigloss.repository;

import com.hainam.worksphere.pigloss.domain.PigLoss;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PigLossRepository extends JpaRepository<PigLoss, UUID> {

    @Query("SELECT p FROM PigLoss p WHERE p.isDeleted = false")
    List<PigLoss> findAllActive();

    @Query("SELECT p FROM PigLoss p WHERE p.id = :id AND p.isDeleted = false")
    Optional<PigLoss> findActiveById(@Param("id") UUID id);


}
