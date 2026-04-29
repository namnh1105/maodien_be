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

    /**
     * Lấy lịch sử lứa đẻ của lợn nái theo pigId (join qua mating_records).
     * Sắp xếp mới nhất lên đầu.
     */
    @Query("SELECT rc FROM ReproductionCycle rc " +
           "JOIN com.hainam.worksphere.mating.domain.Mating m ON m.id = rc.matingId " +
           "WHERE m.sowPigId = :sowPigId AND rc.isDeleted = false AND m.isDeleted = false " +
           "ORDER BY rc.actualFarrowDate DESC NULLS LAST, rc.expectedFarrowDate DESC NULLS LAST")
    List<ReproductionCycle> findActiveBySowPigId(@Param("sowPigId") UUID sowPigId);

    /**
     * Lấy các bản ghi đang mang thai (chưa có actualFarrowDate) theo sowPigId.
     */
    @Query("SELECT rc FROM ReproductionCycle rc " +
           "JOIN com.hainam.worksphere.mating.domain.Mating m ON m.id = rc.matingId " +
           "WHERE m.sowPigId = :sowPigId AND rc.isDeleted = false AND m.isDeleted = false " +
           "AND rc.actualFarrowDate IS NULL " +
           "ORDER BY rc.conceptionDate DESC NULLS LAST")
    List<ReproductionCycle> findActivePregnantBySowPigId(@Param("sowPigId") UUID sowPigId);

    /**
     * Lấy tất cả lợn đang mang thai (chưa đẻ).
     */
    @Query("SELECT rc FROM ReproductionCycle rc " +
           "JOIN com.hainam.worksphere.mating.domain.Mating m ON m.id = rc.matingId " +
           "WHERE rc.isDeleted = false AND m.isDeleted = false " +
           "AND rc.actualFarrowDate IS NULL")
    List<ReproductionCycle> findAllActivePregnant();
}

