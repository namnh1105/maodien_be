package com.hainam.worksphere.penpig.service;

import com.hainam.worksphere.pen.repository.PenRepository;
import com.hainam.worksphere.penpig.domain.PenPig;
import com.hainam.worksphere.penpig.dto.request.CreatePenPigRequest;
import com.hainam.worksphere.penpig.dto.request.UpdatePenPigRequest;
import com.hainam.worksphere.penpig.dto.response.PenPigResponse;
import com.hainam.worksphere.penpig.mapper.PenPigMapper;
import com.hainam.worksphere.penpig.repository.PenPigRepository;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PenPigService {

    private final PenPigRepository penPigRepository;
    private final PenPigMapper penPigMapper;
    private final PigRepository pigRepository;
    private final PenRepository penRepository;
    private final com.hainam.worksphere.pigletherd.repository.PigletHerdRepository pigletHerdRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PEN_PIG")
    public PenPigResponse create(CreatePenPigRequest request, UUID createdBy) {
        // Validate pen exists
        penRepository.findActiveById(request.getPenId())
                .orElseThrow(() -> new ResourceNotFoundException("Pen", request.getPenId().toString()));

        // Validate pig exists (if provided)
        if (request.getPigId() != null) {
            pigRepository.findActiveById(request.getPigId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pig", request.getPigId().toString()));

            // Prevent duplicate active assignment (pig already in this pen with no exit date)
            if (penPigRepository.existsActivByPenIdAndPigId(request.getPenId(), request.getPigId())) {
                throw new BusinessRuleViolationException(
                        "Lợn này đã được phân chuồng vào chuồng đã chọn và chưa xuất chuồng");
            }
        }

        PenPig entity = PenPig.builder()
                .penId(request.getPenId())
                .pigId(request.getPigId())
                .herdId(request.getHerdId())
                .entryDate(request.getEntryDate())
                .exitDate(request.getExitDate())
                .status(request.getStatus())
                .createdBy(createdBy)
                .build();

        PenPig saved = penPigRepository.save(entity);
        AuditContext.registerCreated(saved);
        return toResponseWithEarTagAndHerdName(saved);
    }

    @Transactional(readOnly = true)
    public List<PenPigResponse> getAll() {
        return penPigRepository.findAllActive().stream()
                .map(this::toResponseWithEarTagAndHerdName)
                .toList();
    }

    @Transactional(readOnly = true)
    public PenPigResponse getById(UUID id) {
        PenPig entity = penPigRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PenPig", id.toString()));
        return toResponseWithEarTagAndHerdName(entity);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PEN_PIG")
    public PenPigResponse update(UUID id, UpdatePenPigRequest request, UUID updatedBy) {
        PenPig entity = penPigRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PenPig", id.toString()));

        AuditContext.snapshot(entity);

        if (request.getPenId() != null) entity.setPenId(request.getPenId());
        if (request.getPigId() != null) entity.setPigId(request.getPigId());
        if (request.getHerdId() != null) entity.setHerdId(request.getHerdId());
        if (request.getEntryDate() != null) entity.setEntryDate(request.getEntryDate());
        if (request.getExitDate() != null) entity.setExitDate(request.getExitDate());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        entity.setUpdatedBy(updatedBy);

        PenPig saved = penPigRepository.save(entity);
        AuditContext.registerUpdated(saved);
        return toResponseWithEarTagAndHerdName(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "PEN_PIG")
    public void delete(UUID id, UUID deletedBy) {
        PenPig entity = penPigRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PenPig", id.toString()));

        AuditContext.registerDeleted(entity);

        entity.setIsDeleted(true);
        entity.setDeletedAt(Instant.now());
        entity.setDeletedBy(deletedBy);
        penPigRepository.save(entity);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PEN_PIG")
    public void transfer(com.hainam.worksphere.penpig.dto.request.TransferPenPigRequest request, UUID updatedBy) {
        // Find target pen
        com.hainam.worksphere.pen.domain.Pen targetPen = penRepository.findActiveByName(request.getTargetPenCode())
                .orElseThrow(() -> new ResourceNotFoundException("Pen", request.getTargetPenCode()));

        java.time.LocalDate today = java.time.LocalDate.now();
        List<PenPig> currentAssignments;

        if (request.getPigId() != null) {
            currentAssignments = penPigRepository.findCurrentByPigId(request.getPigId());
        } else if (request.getHerdId() != null) {
            currentAssignments = penPigRepository.findCurrentByHerdId(request.getHerdId());
        } else {
            throw new BusinessRuleViolationException("Phải cung cấp id Lợn hoặc id Đàn");
        }

        // 1. Close current assignments
        for (PenPig assignment : currentAssignments) {
            AuditContext.snapshot(assignment);
            assignment.setExitDate(today);
            assignment.setStatus("TRANSFERRED");
            assignment.setUpdatedBy(updatedBy);
            penPigRepository.save(assignment);
            AuditContext.registerUpdated(assignment);
        }

        // 2. Create new assignment
        PenPig newAssignment = PenPig.builder()
                .penId(targetPen.getId())
                .pigId(request.getPigId())
                .herdId(request.getHerdId())
                .entryDate(today)
                .status("ACTIVE")
                .createdBy(updatedBy)
                .build();

        PenPig saved = penPigRepository.save(newAssignment);
        AuditContext.registerCreated(saved);
    }

    private PenPigResponse toResponseWithEarTagAndHerdName(PenPig penPig) {
        PenPigResponse response = penPigMapper.toResponse(penPig);
        if (penPig.getPigId() != null) {
            pigRepository.findActiveById(penPig.getPigId())
                    .ifPresent(pig -> response.setPigEarTag(pig.getEarTag()));
        }
        if (penPig.getHerdId() != null) {
            pigletHerdRepository.findActiveById(penPig.getHerdId())
                    .ifPresent(herd -> response.setHerdName(herd.getHerdName()));
        }
        return response;
    }
}

