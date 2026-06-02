package com.hainam.worksphere.pigimport.repository;

import com.hainam.worksphere.pigimport.domain.PigImportInvoiceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PigImportInvoiceDetailRepository extends JpaRepository<PigImportInvoiceDetail, UUID> {

    @Query("SELECT d FROM PigImportInvoiceDetail d WHERE d.invoice.id = :invoiceId AND d.isDeleted = false")
    List<PigImportInvoiceDetail> findActiveByInvoiceId(@Param("invoiceId") UUID invoiceId);
}
