package com.hainam.worksphere.materialissuedetail.repository;

import com.hainam.worksphere.materialissuedetail.domain.MaterialIssueDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaterialIssueDetailRepository extends JpaRepository<MaterialIssueDetail, UUID> {

    @Query("SELECT mid FROM MaterialIssueDetail mid WHERE mid.isDeleted = false")
    List<MaterialIssueDetail> findAllActive();

    @Query("SELECT mid FROM MaterialIssueDetail mid WHERE mid.id = :id AND mid.isDeleted = false")
    Optional<MaterialIssueDetail> findActiveById(@Param("id") UUID id);
}
