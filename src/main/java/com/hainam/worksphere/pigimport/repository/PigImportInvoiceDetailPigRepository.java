package com.hainam.worksphere.pigimport.repository;

import com.hainam.worksphere.pigimport.domain.PigImportInvoiceDetailPig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PigImportInvoiceDetailPigRepository extends JpaRepository<PigImportInvoiceDetailPig, UUID> {

    @Query("SELECT p FROM PigImportInvoiceDetailPig p WHERE p.detail.id = :detailId AND p.isDeleted = false")
    List<PigImportInvoiceDetailPig> findActiveByDetailId(@Param("detailId") UUID detailId);

    @Query("SELECT p FROM PigImportInvoiceDetailPig p WHERE p.detail.id IN :detailIds AND p.isDeleted = false")
    List<PigImportInvoiceDetailPig> findActiveByDetailIds(@Param("detailIds") List<UUID> detailIds);
}
