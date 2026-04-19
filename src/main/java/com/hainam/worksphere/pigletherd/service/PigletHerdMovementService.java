package com.hainam.worksphere.pigletherd.service;

import com.hainam.worksphere.pigletherd.domain.PigletHerdMovement;
import com.hainam.worksphere.pigletherd.domain.PigletHerdMovementType;
import com.hainam.worksphere.pigletherd.dto.request.CreatePigletHerdMovementRequest;
import com.hainam.worksphere.pigletherd.dto.request.UpdatePigletHerdMovementRequest;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdMovementResponse;
import com.hainam.worksphere.pigletherd.mapper.PigletHerdMovementMapper;
import com.hainam.worksphere.pigletherd.repository.PigletHerdMovementRepository;
import com.hainam.worksphere.pigletherd.repository.PigletHerdRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.PigletHerdNotFoundException;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PigletHerdMovementService {

    private final PigletHerdMovementRepository pigletHerdMovementRepository;
    private final PigletHerdRepository pigletHerdRepository;
    private final PigletHerdMovementMapper pigletHerdMovementMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIGLET_HERD_MOVEMENT")
    public PigletHerdMovementResponse create(CreatePigletHerdMovementRequest request, UUID createdBy) {
        ensureHerdExists(request.getHerdId());

        PigletHerdMovement movement = PigletHerdMovement.builder()
                .herdId(request.getHerdId())
                .movementType(parseType(request.getMovementType()))
                .sourceHerdId(request.getSourceHerdId())
                .targetHerdId(request.getTargetHerdId())
                .movementDate(request.getMovementDate())
                .quantity(request.getQuantity())
                .reason(request.getReason())
                .createdBy(createdBy)
                .build();

        PigletHerdMovement saved = pigletHerdMovementRepository.save(movement);
        AuditContext.registerCreated(saved);
        return pigletHerdMovementMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PigletHerdMovementResponse> getAll() {
        return pigletHerdMovementRepository.findAllActive().stream().map(pigletHerdMovementMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PigletHerdMovementResponse> getByHerdId(UUID herdId) {
        return pigletHerdMovementRepository.findActiveByHerdId(herdId).stream().map(pigletHerdMovementMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PigletHerdMovementResponse getById(UUID id) {
        PigletHerdMovement movement = pigletHerdMovementRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigletHerdMovement", id.toString()));
        return pigletHerdMovementMapper.toResponse(movement);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PIGLET_HERD_MOVEMENT")
    public PigletHerdMovementResponse update(UUID id, UpdatePigletHerdMovementRequest request, UUID updatedBy) {
        PigletHerdMovement movement = pigletHerdMovementRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigletHerdMovement", id.toString()));

        AuditContext.snapshot(movement);

        if (request.getMovementType() != null) movement.setMovementType(parseType(request.getMovementType()));
        if (request.getSourceHerdId() != null) movement.setSourceHerdId(request.getSourceHerdId());
        if (request.getTargetHerdId() != null) movement.setTargetHerdId(request.getTargetHerdId());
        if (request.getMovementDate() != null) movement.setMovementDate(request.getMovementDate());
        if (request.getQuantity() != null) movement.setQuantity(request.getQuantity());
        if (request.getReason() != null) movement.setReason(request.getReason());
        movement.setUpdatedBy(updatedBy);

        PigletHerdMovement saved = pigletHerdMovementRepository.save(movement);
        AuditContext.registerUpdated(saved);
        return pigletHerdMovementMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "PIGLET_HERD_MOVEMENT")
    public void delete(UUID id, UUID deletedBy) {
        PigletHerdMovement movement = pigletHerdMovementRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigletHerdMovement", id.toString()));

        AuditContext.registerDeleted(movement);

        movement.setIsDeleted(true);
        movement.setDeletedAt(Instant.now());
        movement.setDeletedBy(deletedBy);
        pigletHerdMovementRepository.save(movement);
    }

    private PigletHerdMovementType parseType(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            throw new BusinessRuleViolationException("Movement type is required");
        }
        try {
            return PigletHerdMovementType.valueOf(rawType.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleViolationException("Invalid movement type: " + rawType);
        }
    }

    private void ensureHerdExists(UUID herdId) {
        pigletHerdRepository.findActiveById(herdId)
                .orElseThrow(() -> PigletHerdNotFoundException.byId(herdId.toString()));
    }
}
