package com.hainam.worksphere.medicine.repository;

import com.hainam.worksphere.medicine.domain.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, UUID> {

    @Query("SELECT m FROM Medicine m WHERE m.isDeleted = false")
    List<Medicine> findAllActive();

    @Query("SELECT m FROM Medicine m WHERE m.id = :id AND m.isDeleted = false")
    Optional<Medicine> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM Medicine m WHERE m.name = :name AND m.isDeleted = false")
    boolean existsActiveByName(@Param("name") String name);
}
