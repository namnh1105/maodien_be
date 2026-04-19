package com.hainam.worksphere.materialreceiptdetail.repository;

import com.hainam.worksphere.materialreceiptdetail.domain.MaterialReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaterialReceiptDetailRepository extends JpaRepository<MaterialReceiptDetail, UUID> {

    @Query("SELECT mrd FROM MaterialReceiptDetail mrd WHERE mrd.isDeleted = false")
    List<MaterialReceiptDetail> findAllActive();

    @Query("SELECT mrd FROM MaterialReceiptDetail mrd WHERE mrd.id = :id AND mrd.isDeleted = false")
    Optional<MaterialReceiptDetail> findActiveById(@Param("id") UUID id);
}
