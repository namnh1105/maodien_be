package com.hainam.worksphere.reproductioncycle.repository;

import com.hainam.worksphere.reproductioncycle.domain.ReproductionCycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReproductionCycleRepository extends JpaRepository<ReproductionCycle, UUID> {

    @Query("SELECT rc FROM ReproductionCycle rc WHERE rc.isDeleted = false")
    List<ReproductionCycle> findAllActive();

    @Query("SELECT rc FROM ReproductionCycle rc WHERE rc.id = :id AND rc.isDeleted = false")
    Optional<ReproductionCycle> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(rc) > 0 THEN true ELSE false END FROM ReproductionCycle rc WHERE rc.cycleCode = :code AND rc.isDeleted = false")
    boolean existsActiveByCycleCode(@Param("code") String code);
}
