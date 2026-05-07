package com.hainam.worksphere.cullingproposal.service;

import com.hainam.worksphere.cullingproposal.domain.CullingProposal;
import com.hainam.worksphere.cullingproposal.dto.request.CreateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.request.UpdateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.request.UpdateCullingProposalStatusRequest;
import com.hainam.worksphere.cullingproposal.dto.response.CullingProposalResponse;
import com.hainam.worksphere.cullingproposal.mapper.CullingProposalMapper;
import com.hainam.worksphere.cullingproposal.repository.CullingProposalRepository;
import com.hainam.worksphere.employee.repository.EmployeeRepository;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.text.Normalizer;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CullingProposalService {

    private final CullingProposalRepository cullingProposalRepository;
    private final CullingProposalMapper cullingProposalMapper;
    private final PigRepository pigRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "CULLING_PROPOSAL")
    public CullingProposalResponse create(CreateCullingProposalRequest request, UUID createdBy) {
        CullingProposal cullingProposal = cullingProposalMapper.toEntity(request);
        cullingProposal.setStatus(normalizeStatus(request.getStatus()));
        cullingProposal.setCreatedBy(createdBy);

        CullingProposal saved = cullingProposalRepository.save(cullingProposal);
        AuditContext.registerCreated(saved);
        return toResponseWithEarTag(saved);
    }

    @Transactional(readOnly = true)
    public List<CullingProposalResponse> getAll() {
        return cullingProposalRepository.findAllActive().stream()
                .map(this::toResponseWithEarTag)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CullingProposalResponse> getByProposalType(String proposalType) {
        return cullingProposalRepository.findActiveByProposalType(proposalType).stream()
                .map(this::toResponseWithEarTag)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CullingProposalResponse> getProcessed() {
        return cullingProposalRepository.findAllActive().stream()
                .filter(c -> !isPendingStatus(c.getStatus()))
                .map(this::toResponseWithEarTag)
                .toList();
    }

    @Transactional(readOnly = true)
    public CullingProposalResponse getById(UUID id) {
        CullingProposal cullingProposal = cullingProposalRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CullingProposal", id));
        return toResponseWithEarTag(cullingProposal);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "CULLING_PROPOSAL")
    public CullingProposalResponse update(UUID id, UpdateCullingProposalRequest request, UUID updatedBy) {
        CullingProposal cullingProposal = cullingProposalRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CullingProposal", id));

        AuditContext.snapshot(cullingProposal);
        cullingProposalMapper.updateEntityFromRequest(request, cullingProposal);
        if (request.getStatus() != null) {
            cullingProposal.setStatus(normalizeStatus(request.getStatus()));
        }
        cullingProposal.setUpdatedBy(updatedBy);

        CullingProposal saved = cullingProposalRepository.save(cullingProposal);
        AuditContext.registerUpdated(saved);
        return toResponseWithEarTag(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "CULLING_PROPOSAL", actionCode = "REVIEW_CULLING_PROPOSAL")
    public List<CullingProposalResponse> review(List<UpdateCullingProposalStatusRequest> requests, UUID updatedBy) {
        return requests.stream().map(request -> {
            CullingProposal cullingProposal = cullingProposalRepository.findActiveById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("CullingProposal", request.getId()));

            AuditContext.snapshot(cullingProposal);
            cullingProposal.setStatus(normalizeStatus(request.getStatus()));
            cullingProposal.setUpdatedBy(updatedBy);

            CullingProposal saved = cullingProposalRepository.save(cullingProposal);
            AuditContext.registerUpdated(saved);
            return toResponseWithEarTag(saved);
        }).toList();
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

    private CullingProposalResponse toResponseWithEarTag(CullingProposal cullingProposal) {
        CullingProposalResponse response = cullingProposalMapper.toResponse(cullingProposal);
        if (cullingProposal.getPigId() != null) {
            pigRepository.findActiveById(cullingProposal.getPigId())
                    .ifPresent(pig -> response.setPigEarTag(pig.getEarTag()));
        }
        if (cullingProposal.getEmployeeId() != null) {
            employeeRepository.findActiveById(cullingProposal.getEmployeeId())
                    .ifPresent(employee -> response.setEmployeeName(employee.getFullName()));
        }
        return response;
    }

    private String normalizeStatus(String status) {
        if (status == null || status.isBlank()) {
            return "Chờ duyệt";
        }
        return status.trim();
    }

    private boolean isPendingStatus(String status) {
        if (status == null || status.isBlank()) {
            return true;
        }
        String normalized = normalizeText(status);
        return normalized.equals("cho duyet") || normalized.equals("dang cho") || normalized.equals("pending");
    }

    private String normalizeText(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.trim().toLowerCase();
    }
}

