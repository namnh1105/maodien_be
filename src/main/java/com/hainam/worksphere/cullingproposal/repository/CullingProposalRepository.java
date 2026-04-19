package com.hainam.worksphere.cullingproposal.repository;

import com.hainam.worksphere.cullingproposal.domain.CullingProposal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CullingProposalRepository extends JpaRepository<CullingProposal, UUID> {

    @Query("SELECT c FROM CullingProposal c WHERE c.isDeleted = false")
    List<CullingProposal> findAllActive();

    @Query("SELECT c FROM CullingProposal c WHERE c.id = :id AND c.isDeleted = false")
    Optional<CullingProposal> findActiveById(@Param("id") UUID id);
}
