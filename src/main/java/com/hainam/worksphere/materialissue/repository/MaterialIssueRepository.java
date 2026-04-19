package com.hainam.worksphere.materialissue.repository;

import com.hainam.worksphere.materialissue.domain.MaterialIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaterialIssueRepository extends JpaRepository<MaterialIssue, UUID> {

    @Query("SELECT mi FROM MaterialIssue mi WHERE mi.isDeleted = false")
    List<MaterialIssue> findAllActive();

    @Query("SELECT mi FROM MaterialIssue mi WHERE mi.id = :id AND mi.isDeleted = false")
    Optional<MaterialIssue> findActiveById(@Param("id") UUID id);
}
