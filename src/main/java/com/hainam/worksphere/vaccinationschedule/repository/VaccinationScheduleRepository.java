package com.hainam.worksphere.vaccinationschedule.repository;

import com.hainam.worksphere.vaccinationschedule.domain.VaccinationSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VaccinationScheduleRepository extends JpaRepository<VaccinationSchedule, UUID> {

    @Query("SELECT vs FROM VaccinationSchedule vs WHERE vs.isDeleted = false")
    List<VaccinationSchedule> findAllActive();

    @Query("SELECT vs FROM VaccinationSchedule vs WHERE vs.id = :id AND vs.isDeleted = false")
    Optional<VaccinationSchedule> findActiveById(@Param("id") UUID id);
}
