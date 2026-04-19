package com.hainam.worksphere.workschedule.repository;

import com.hainam.worksphere.workschedule.domain.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, UUID> {

    @Query("SELECT w FROM WorkSchedule w WHERE w.isDeleted = false")
    List<WorkSchedule> findAllActive();

    @Query("SELECT w FROM WorkSchedule w WHERE w.id = :id AND w.isDeleted = false")
    Optional<WorkSchedule> findActiveById(@Param("id") UUID id);
}
