package com.hainam.worksphere.penpig.repository;

import com.hainam.worksphere.penpig.domain.PenPig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PenPigRepository extends JpaRepository<PenPig, UUID> {

    @Query("SELECT pp FROM PenPig pp WHERE pp.isDeleted = false")
    List<PenPig> findAllActive();

    @Query("SELECT pp FROM PenPig pp WHERE pp.id = :id AND pp.isDeleted = false")
    Optional<PenPig> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(pp) > 0 THEN true ELSE false END FROM PenPig pp WHERE pp.assignmentCode = :code AND pp.isDeleted = false")
    boolean existsActiveByAssignmentCode(@Param("code") String code);

    @Query("SELECT pp FROM PenPig pp WHERE pp.penId = :penId AND pp.isDeleted = false AND pp.exitDate IS NULL")
    List<PenPig> findCurrentByPenId(@Param("penId") UUID penId);

    @Query("SELECT pp FROM PenPig pp WHERE pp.pigId = :pigId AND pp.isDeleted = false AND pp.exitDate IS NULL")
    List<PenPig> findCurrentByPigId(@Param("pigId") UUID pigId);
}
