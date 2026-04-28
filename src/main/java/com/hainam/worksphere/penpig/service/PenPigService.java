package com.hainam.worksphere.penpig.service;

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

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PEN_PIG")
    public PenPigResponse create(CreatePenPigRequest request, UUID createdBy) {
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
        return toResponseWithEarTag(saved);
    }

    @Transactional(readOnly = true)
    public List<PenPigResponse> getAll() {
        return penPigRepository.findAllActive().stream()
                .map(this::toResponseWithEarTag)
                .toList();
    }

    @Transactional(readOnly = true)
    public PenPigResponse getById(UUID id) {
        PenPig entity = penPigRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PenPig", id.toString()));
        return toResponseWithEarTag(entity);
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
        return toResponseWithEarTag(saved);
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

    private PenPigResponse toResponseWithEarTag(PenPig penPig) {
        PenPigResponse response = penPigMapper.toResponse(penPig);
        if (penPig.getPigId() != null) {
            pigRepository.findActiveById(penPig.getPigId())
                    .ifPresent(pig -> response.setPigEarTag(pig.getEarTag()));
        }
        return response;
    }
}

