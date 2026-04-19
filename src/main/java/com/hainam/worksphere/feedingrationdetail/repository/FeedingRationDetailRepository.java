package com.hainam.worksphere.feedingrationdetail.repository;

import com.hainam.worksphere.feedingrationdetail.domain.FeedingRationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedingRationDetailRepository extends JpaRepository<FeedingRationDetail, UUID> {

    @Query("SELECT frd FROM FeedingRationDetail frd WHERE frd.isDeleted = false")
    List<FeedingRationDetail> findAllActive();

    @Query("SELECT frd FROM FeedingRationDetail frd WHERE frd.id = :id AND frd.isDeleted = false")
    Optional<FeedingRationDetail> findActiveById(@Param("id") UUID id);
}
