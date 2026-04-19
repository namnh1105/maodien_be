package com.hainam.worksphere.cullingproposal.service;

import com.hainam.worksphere.cullingproposal.domain.CullingProposal;
import com.hainam.worksphere.cullingproposal.dto.request.CreateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.request.UpdateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.response.CullingProposalResponse;
import com.hainam.worksphere.cullingproposal.mapper.CullingProposalMapper;
import com.hainam.worksphere.cullingproposal.repository.CullingProposalRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CullingProposalService {

    private final CullingProposalRepository cullingProposalRepository;
    private final CullingProposalMapper cullingProposalMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "CULLING_PROPOSAL")
    public CullingProposalResponse create(CreateCullingProposalRequest request, UUID createdBy) {
        CullingProposal cullingProposal = cullingProposalMapper.toEntity(request);
        cullingProposal.setCreatedBy(createdBy);

        CullingProposal saved = cullingProposalRepository.save(cullingProposal);
        AuditContext.registerCreated(saved);
        return cullingProposalMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CullingProposalResponse> getAll() {
        return cullingProposalRepository.findAllActive().stream().map(cullingProposalMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CullingProposalResponse getById(UUID id) {
        CullingProposal cullingProposal = cullingProposalRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CullingProposal", id));
        return cullingProposalMapper.toResponse(cullingProposal);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "CULLING_PROPOSAL")
    public CullingProposalResponse update(UUID id, UpdateCullingProposalRequest request, UUID updatedBy) {
        CullingProposal cullingProposal = cullingProposalRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CullingProposal", id));

        AuditContext.snapshot(cullingProposal);
        cullingProposalMapper.updateEntityFromRequest(request, cullingProposal);
        cullingProposal.setUpdatedBy(updatedBy);

        CullingProposal saved = cullingProposalRepository.save(cullingProposal);
        AuditContext.registerUpdated(saved);
        return cullingProposalMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "CULLING_PROPOSAL")
    public void delete(UUID id, UUID deletedBy) {
        CullingProposal cullingProposal = cullingProposalRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CullingProposal", id));

        AuditContext.registerDeleted(cullingProposal);
        cullingProposal.setIsDeleted(true);
        cullingProposal.setDeletedAt(Instant.now());
        cullingProposal.setDeletedBy(deletedBy);
        cullingProposalRepository.save(cullingProposal);
    }
}
