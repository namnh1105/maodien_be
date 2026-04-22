package com.hainam.worksphere.vaccine.repository;

import com.hainam.worksphere.vaccine.domain.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, UUID> {

    @Query("SELECT v FROM Vaccine v WHERE v.isDeleted = false")
    List<Vaccine> findAllActive();

    @Query("SELECT v FROM Vaccine v WHERE v.id = :id AND v.isDeleted = false")
    Optional<Vaccine> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Vaccine v WHERE v.name = :name AND v.isDeleted = false")
    boolean existsActiveByName(@Param("name") String name);
}
