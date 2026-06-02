package com.hainam.worksphere.pigimport.repository;

import com.hainam.worksphere.pigimport.domain.PigImportInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PigImportInvoiceRepository extends JpaRepository<PigImportInvoice, UUID> {

    @Query("SELECT i FROM PigImportInvoice i WHERE i.isDeleted = false")
    List<PigImportInvoice> findAllActive();

    @Query("SELECT i FROM PigImportInvoice i WHERE i.id = :id AND i.isDeleted = false")
    Optional<PigImportInvoice> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(i) > 0 THEN true ELSE false END FROM PigImportInvoice i WHERE i.invoiceCode = :invoiceCode AND i.isDeleted = false")
    boolean existsActiveByInvoiceCode(@Param("invoiceCode") String invoiceCode);
}
