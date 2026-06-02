package com.hainam.worksphere.sale.repository;

import com.hainam.worksphere.sale.domain.Sale;
import com.hainam.worksphere.sale.domain.SaleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDate;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {

    @Query("SELECT s FROM Sale s WHERE s.isDeleted = false")
    List<Sale> findAllActive();

    @Query("SELECT s FROM Sale s WHERE s.id = :id AND s.isDeleted = false")
    Optional<Sale> findActiveById(@Param("id") UUID id);

    @Query("SELECT s FROM Sale s WHERE s.customer.id = :customerId AND s.isDeleted = false")
    List<Sale> findActiveByCustomerId(@Param("customerId") UUID customerId);

    @Query("SELECT s FROM Sale s WHERE s.pig.id = :pigId AND s.isDeleted = false")
    List<Sale> findActiveByPigId(@Param("pigId") UUID pigId);

    @Query("SELECT s FROM Sale s WHERE s.saleDate BETWEEN :startDate AND :endDate AND s.isDeleted = false")
    List<Sale> findActiveBySaleDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT s FROM Sale s WHERE s.saleType = :saleType AND s.saleDate BETWEEN :startDate AND :endDate AND s.isDeleted = false")
    List<Sale> findActiveBySaleTypeAndSaleDateBetween(
            @Param("saleType") SaleType saleType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
