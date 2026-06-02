package com.hainam.worksphere.feedingrationdetail.repository;

import com.hainam.worksphere.feedingrationdetail.domain.FeedingRationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;

@Repository
public interface FeedingRationDetailRepository extends JpaRepository<FeedingRationDetail, UUID> {

    @Query("SELECT frd FROM FeedingRationDetail frd WHERE frd.isDeleted = false")
    List<FeedingRationDetail> findAllActive();

    @Query("SELECT frd FROM FeedingRationDetail frd WHERE frd.id = :id AND frd.isDeleted = false")
    Optional<FeedingRationDetail> findActiveById(@Param("id") UUID id);

    @Query("SELECT fr.rationDate, SUM(frd.totalFeedAmount) " +
           "FROM FeedingRationDetail frd JOIN com.hainam.worksphere.feedingration.domain.FeedingRation fr ON fr.id = frd.rationId " +
           "WHERE fr.isDeleted = false AND frd.isDeleted = false AND fr.rationDate BETWEEN :startDate AND :endDate " +
           "GROUP BY fr.rationDate")
    List<Object[]> sumTotalFeedByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(frd.totalFeedAmount), 0) " +
           "FROM FeedingRationDetail frd JOIN com.hainam.worksphere.feedingration.domain.FeedingRation fr ON fr.id = frd.rationId " +
           "WHERE fr.isDeleted = false AND frd.isDeleted = false " +
           "AND fr.penId = :penId AND fr.rationDate BETWEEN :startDate AND :endDate")
    Double sumTotalFeedByPenAndDateRange(
            @Param("penId") UUID penId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
