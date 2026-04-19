package com.hainam.worksphere.feedingration.repository;

import com.hainam.worksphere.feedingration.domain.FeedingRation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FeedingRationRepository extends JpaRepository<FeedingRation, UUID> {

    @Query("SELECT fr FROM FeedingRation fr WHERE fr.isDeleted = false")
    List<FeedingRation> findAllActive();

    @Query("SELECT fr FROM FeedingRation fr WHERE fr.id = :id AND fr.isDeleted = false")
    Optional<FeedingRation> findActiveById(@Param("id") UUID id);

    @Query("SELECT fr FROM FeedingRation fr WHERE fr.penId = :penId AND fr.isDeleted = false ORDER BY fr.rationDate DESC, fr.createdAt DESC")
    List<FeedingRation> findActiveByPenIdOrderByLatest(@Param("penId") UUID penId);
}
