package com.hainam.worksphere.pigletherd.repository;

import com.hainam.worksphere.pigletherd.domain.PigletHerdSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface PigletHerdSaleRepository extends JpaRepository<PigletHerdSale, UUID> {

    @Query("SELECT s FROM PigletHerdSale s WHERE s.isDeleted = false")
    List<PigletHerdSale> findAllActive();

    @Query("SELECT s FROM PigletHerdSale s WHERE s.saleDate BETWEEN :startDate AND :endDate AND s.isDeleted = false")
    List<PigletHerdSale> findActiveBySaleDateBetween(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
