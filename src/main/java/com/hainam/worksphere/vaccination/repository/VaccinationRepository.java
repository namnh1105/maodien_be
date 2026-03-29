package com.hainam.worksphere.vaccination.repository;

import com.hainam.worksphere.vaccination.domain.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, UUID> {

    @Query("SELECT v FROM Vaccination v WHERE v.isDeleted = false")
    List<Vaccination> findAllActive();

    @Query("SELECT v FROM Vaccination v WHERE v.id = :id AND v.isDeleted = false")
    Optional<Vaccination> findActiveById(@Param("id") UUID id);

    @Query("SELECT v FROM Vaccination v WHERE v.pig.id = :pigId AND v.isDeleted = false")
    List<Vaccination> findActiveByPigId(@Param("pigId") UUID pigId);

    @Query("SELECT v FROM Vaccination v WHERE v.employee.id = :employeeId AND v.isDeleted = false")
    List<Vaccination> findActiveByEmployeeId(@Param("employeeId") UUID employeeId);
}
