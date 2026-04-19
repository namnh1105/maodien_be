package com.hainam.worksphere.area.repository;

import com.hainam.worksphere.area.domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AreaRepository extends JpaRepository<Area, UUID> {

    @Query("SELECT a FROM Area a WHERE a.isDeleted = false")
    List<Area> findAllActive();

    @Query("SELECT a FROM Area a WHERE a.id = :id AND a.isDeleted = false")
    Optional<Area> findActiveById(@Param("id") UUID id);
}
