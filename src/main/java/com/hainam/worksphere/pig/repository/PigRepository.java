package com.hainam.worksphere.pig.repository;

import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.domain.PigStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PigRepository extends JpaRepository<Pig, UUID> {

    @Query("SELECT p FROM Pig p WHERE p.isDeleted = false")
    List<Pig> findAllActive();

    @Query("SELECT p FROM Pig p WHERE p.id = :id AND p.isDeleted = false")
    Optional<Pig> findActiveById(@Param("id") UUID id);

    @Query("SELECT p FROM Pig p WHERE p.pigCode = :code AND p.isDeleted = false")
    Optional<Pig> findActiveByPigCode(@Param("code") String code);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Pig p WHERE p.pigCode = :code AND p.isDeleted = false")
    boolean existsActiveByPigCode(@Param("code") String code);

    @Query("SELECT p FROM Pig p WHERE p.status = :status AND p.isDeleted = false")
    List<Pig> findActiveByStatus(@Param("status") PigStatus status);
}
