package com.hainam.worksphere.materialreceipt.repository;

import com.hainam.worksphere.materialreceipt.domain.MaterialReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MaterialReceiptRepository extends JpaRepository<MaterialReceipt, UUID> {

    @Query("SELECT mr FROM MaterialReceipt mr WHERE mr.isDeleted = false")
    List<MaterialReceipt> findAllActive();

    @Query("SELECT mr FROM MaterialReceipt mr WHERE mr.id = :id AND mr.isDeleted = false")
    Optional<MaterialReceipt> findActiveById(@Param("id") UUID id);
}
