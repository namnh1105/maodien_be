package com.hainam.worksphere.disease.repository;

import com.hainam.worksphere.disease.domain.Disease;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DiseaseRepository extends JpaRepository<Disease, UUID> {

    @Query("SELECT d FROM Disease d WHERE d.isDeleted = false")
    List<Disease> findAllActive();

    @Query("SELECT d FROM Disease d WHERE d.id = :id AND d.isDeleted = false")
    Optional<Disease> findActiveById(@Param("id") UUID id);
}
