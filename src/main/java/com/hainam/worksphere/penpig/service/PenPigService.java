package com.hainam.worksphere.penpig.service;

import com.hainam.worksphere.penpig.domain.PenPig;
import com.hainam.worksphere.penpig.dto.request.CreatePenPigRequest;
import com.hainam.worksphere.penpig.dto.request.UpdatePenPigRequest;
import com.hainam.worksphere.penpig.dto.response.PenPigResponse;
import com.hainam.worksphere.penpig.mapper.PenPigMapper;
import com.hainam.worksphere.penpig.repository.PenPigRepository;
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

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PEN_PIG")
    public PenPigResponse create(CreatePenPigRequest request, UUID createdBy) {
        if (penPigRepository.existsActiveByAssignmentCode(null)) {
            throw new BusinessRuleViolationException("Pen pig assignment code already exists: " + null);
        }

        PenPig entity = PenPig.builder()
                .penId(request.getPenId())
                .pigId(request.getPigId())
                .entryDate(request.getEntryDate())
                .exitDate(request.getExitDate())
                .status(request.getStatus())
                .createdBy(createdBy)
                .build();

        PenPig saved = penPigRepository.save(entity);
        AuditContext.registerCreated(saved);
        return penPigMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PenPigResponse> getAll() {
        return penPigRepository.findAllActive().stream().map(penPigMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PenPigResponse getById(UUID id) {
        PenPig entity = penPigRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PenPig", id.toString()));
        return penPigMapper.toResponse(entity);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PEN_PIG")
    public PenPigResponse update(UUID id, UpdatePenPigRequest request, UUID updatedBy) {
        PenPig entity = penPigRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PenPig", id.toString()));

        AuditContext.snapshot(entity);

        if (request.getPenId() != null) entity.setPenId(request.getPenId());
        if (request.getPigId() != null) entity.setPigId(request.getPigId());
        if (request.getEntryDate() != null) entity.setEntryDate(request.getEntryDate());
        if (request.getExitDate() != null) entity.setExitDate(request.getExitDate());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        entity.setUpdatedBy(updatedBy);

        PenPig saved = penPigRepository.save(entity);
        AuditContext.registerUpdated(saved);
        return penPigMapper.toResponse(saved);
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
}
