package com.hainam.worksphere.pen.repository;

import com.hainam.worksphere.pen.domain.Pen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PenRepository extends JpaRepository<Pen, UUID> {

    @Query("SELECT p FROM Pen p WHERE p.isDeleted = false")
    List<Pen> findAllActive();

    @Query("SELECT p FROM Pen p WHERE p.id = :id AND p.isDeleted = false")
    Optional<Pen> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Pen p WHERE p.penCode = :code AND p.isDeleted = false")
    boolean existsActiveByPenCode(@Param("code") String code);
}
