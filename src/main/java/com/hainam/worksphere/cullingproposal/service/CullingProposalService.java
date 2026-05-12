package com.hainam.worksphere.cullingproposal.service;

import com.hainam.worksphere.cullingproposal.domain.CullingProposal;
import com.hainam.worksphere.cullingproposal.dto.request.CreateCullingProposalBulkRequest;
import com.hainam.worksphere.cullingproposal.dto.request.CreateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.request.UpdateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.request.UpdateCullingProposalStatusRequest;
import com.hainam.worksphere.cullingproposal.dto.response.CullingProposalResponse;
import com.hainam.worksphere.cullingproposal.mapper.CullingProposalMapper;
import com.hainam.worksphere.cullingproposal.repository.CullingProposalRepository;
import com.hainam.worksphere.employee.domain.Employee;
import com.hainam.worksphere.employee.repository.EmployeeRepository;
import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.domain.PigStatus;
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

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "CULLING_PROPOSAL", actionCode = "BULK_CREATE")
    public List<CullingProposalResponse> createBulk(List<CreateCullingProposalRequest> requests, UUID createdBy) {
        List<CullingProposal> entities = requests.stream().map(request -> {
            CullingProposal cullingProposal = cullingProposalMapper.toEntity(request);
            cullingProposal.setStatus(normalizeStatus(request.getStatus()));
            cullingProposal.setCreatedBy(createdBy);
            return cullingProposal;
        }).toList();

        List<CullingProposal> savedList = cullingProposalRepository.saveAll(entities);
        savedList.forEach(AuditContext::registerCreated);

        return savedList.stream()
                .map(this::toResponseWithEarTag)
                .toList();
    }

        @Transactional
        @AuditAction(type = ActionType.CREATE, entity = "CULLING_PROPOSAL", actionCode = "BULK_CREATE_BY_EAR_TAG")
        public List<CullingProposalResponse> createBulkByEarTag(List<CreateCullingProposalBulkRequest> requests, UUID userId) {
        Employee employee = employeeRepository.findActiveByUserId(userId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", userId.toString()));

        List<CullingProposal> entities = requests.stream().map(request -> {
            Pig pig = pigRepository.findActiveByEarTag(request.getPigEarTag())
                .orElseThrow(() -> new ResourceNotFoundException("Pig", request.getPigEarTag()));

            return CullingProposal.builder()
                .pigId(pig.getId())
                .proposalType(request.getProposalType())
                .reason(request.getReason())
                .employeeId(employee.getId())
                .status(normalizeStatus(null))
                .createdBy(userId)
                .build();
        }).toList();

        List<CullingProposal> savedList = cullingProposalRepository.saveAll(entities);
        savedList.forEach(AuditContext::registerCreated);

        return savedList.stream()
            .map(this::toResponseWithEarTag)
            .toList();
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
            String normalizedStatus = normalizeStatus(request.getStatus());
            cullingProposal.setStatus(normalizedStatus);
            cullingProposal.setUpdatedBy(updatedBy);

            CullingProposal saved = cullingProposalRepository.save(cullingProposal);
            AuditContext.registerUpdated(saved);

            if (isApprovedStatus(normalizedStatus)) {
                updatePigStatusForApprovedProposal(saved, updatedBy);
            }
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

    private boolean isApprovedStatus(String status) {
        if (status == null || status.isBlank()) {
            return false;
        }
        String normalized = normalizeText(status);
        return normalized.contains("duyet") || normalized.contains("approve") || normalized.contains("approved");
    }

    private void updatePigStatusForApprovedProposal(CullingProposal proposal, UUID updatedBy) {
        if (proposal.getPigId() == null) {
            return;
        }

        PigStatus nextStatus = resolvePigStatusForApprovedProposal(proposal.getProposalType());
        if (nextStatus == null) {
            return;
        }

        Pig pig = pigRepository.findActiveById(proposal.getPigId())
                .orElseThrow(() -> new ResourceNotFoundException("Pig", proposal.getPigId().toString()));

        if (pig.getStatus() == PigStatus.ACTIVE) {
            pig.setStatus(nextStatus);
            pig.setUpdatedBy(updatedBy);
            Pig savedPig = pigRepository.save(pig);
            AuditContext.registerUpdated(savedPig);
        }
    }

    private PigStatus resolvePigStatusForApprovedProposal(String proposalType) {
        if (proposalType == null || proposalType.isBlank()) {
            return null;
        }
        String normalized = normalizeText(proposalType);
        if (normalized.contains("tieu huy")) {
            return PigStatus.CULLING;
        }
        if (normalized.contains("ban loai")) {
            return PigStatus.THIT;
        }
        return null;
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

