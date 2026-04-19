package com.hainam.worksphere.pen.service;

import com.hainam.worksphere.pen.domain.Pen;
import com.hainam.worksphere.pen.domain.PenStatus;
import com.hainam.worksphere.pen.domain.PenType;
import com.hainam.worksphere.pen.dto.request.CreatePenRequest;
import com.hainam.worksphere.pen.dto.request.UpdatePenRequest;
import com.hainam.worksphere.pen.dto.response.PenResponse;
import com.hainam.worksphere.pen.mapper.PenMapper;
import com.hainam.worksphere.pen.repository.PenRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.PenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PenService {

    private final PenRepository penRepository;
    private final PenMapper penMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PEN")
    public PenResponse create(CreatePenRequest request, UUID createdBy) {
        Pen pen = Pen.builder()
                .name(request.getName())
                .area(request.getArea())
                .areaId(request.getAreaId())
                .penType(parsePenType(request.getPenType()))
                .status(parsePenStatus(request.getStatus()))
                .createdBy(createdBy)
                .build();

        Pen saved = penRepository.save(pen);
        saved.setPenCode(generatePenCode(saved.getId()));
        saved = penRepository.save(saved);
        AuditContext.registerCreated(saved);
        return penMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PenResponse> getAll() {
        return penRepository.findAllActive().stream().map(penMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PenResponse getById(UUID id) {
        Pen pen = penRepository.findActiveById(id)
                .orElseThrow(() -> PenNotFoundException.byId(id.toString()));
        return penMapper.toResponse(pen);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PEN")
    public PenResponse update(UUID id, UpdatePenRequest request, UUID updatedBy) {
        Pen pen = penRepository.findActiveById(id)
                .orElseThrow(() -> PenNotFoundException.byId(id.toString()));

        AuditContext.snapshot(pen);

        if (request.getName() != null) pen.setName(request.getName());
        if (request.getArea() != null) pen.setArea(request.getArea());
        if (request.getAreaId() != null) pen.setAreaId(request.getAreaId());
        if (request.getPenType() != null) pen.setPenType(parsePenType(request.getPenType()));
        if (request.getStatus() != null) pen.setStatus(parsePenStatus(request.getStatus()));
        pen.setUpdatedBy(updatedBy);

        Pen saved = penRepository.save(pen);
        AuditContext.registerUpdated(saved);
        return penMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "PEN")
    public void delete(UUID id, UUID deletedBy) {
        Pen pen = penRepository.findActiveById(id)
                .orElseThrow(() -> PenNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(pen);

        pen.setIsDeleted(true);
        pen.setDeletedAt(Instant.now());
        pen.setDeletedBy(deletedBy);
        penRepository.save(pen);
    }

    private PenType parsePenType(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            return null;
        }
        try {
            return PenType.valueOf(rawType.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleViolationException("Invalid pen type: " + rawType);
        }
    }

    private PenStatus parsePenStatus(String rawStatus) {
        if (rawStatus == null || rawStatus.isBlank()) {
            return PenStatus.EMPTY;
        }
        try {
            return PenStatus.valueOf(rawStatus.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleViolationException("Invalid pen status: " + rawStatus);
        }
    }

    private String generatePenCode(UUID id) {
        String shortId = id.toString().replace("-", "").substring(0, 8).toUpperCase();
        return "PEN-" + shortId;
    }
}
