package com.hainam.worksphere.pigloss.service;

import com.hainam.worksphere.pigloss.domain.PigLoss;
import com.hainam.worksphere.pigloss.dto.request.CreatePigLossRequest;
import com.hainam.worksphere.pigloss.dto.request.UpdatePigLossRequest;
import com.hainam.worksphere.pigloss.dto.response.PigLossResponse;
import com.hainam.worksphere.pigloss.mapper.PigLossMapper;
import com.hainam.worksphere.pigloss.repository.PigLossRepository;
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
public class PigLossService {

    private final PigLossRepository pigLossRepository;
    private final PigLossMapper pigLossMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIG_LOSS")
    public PigLossResponse create(CreatePigLossRequest request, UUID createdBy) {
        PigLoss entity = PigLoss.builder()
                .pigId(request.getPigId())
                .lossDate(request.getLossDate())
                .reason(request.getReason())
                .note(request.getNote())
                .damageValue(request.getDamageValue())
                .employeeId(request.getEmployeeId())
                .createdBy(createdBy)
                .build();

        PigLoss saved = pigLossRepository.save(entity);
        AuditContext.registerCreated(saved);
        return pigLossMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PigLossResponse> getAll() {
        return pigLossRepository.findAllActive().stream().map(pigLossMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PigLossResponse getById(UUID id) {
        PigLoss entity = pigLossRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigLoss", id.toString()));
        return pigLossMapper.toResponse(entity);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PIG_LOSS")
    public PigLossResponse update(UUID id, UpdatePigLossRequest request, UUID updatedBy) {
        PigLoss entity = pigLossRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigLoss", id.toString()));

        AuditContext.snapshot(entity);

        if (request.getPigId() != null) entity.setPigId(request.getPigId());
        if (request.getLossDate() != null) entity.setLossDate(request.getLossDate());
        if (request.getReason() != null) entity.setReason(request.getReason());
        if (request.getNote() != null) entity.setNote(request.getNote());
        if (request.getDamageValue() != null) entity.setDamageValue(request.getDamageValue());
        if (request.getEmployeeId() != null) entity.setEmployeeId(request.getEmployeeId());
        entity.setUpdatedBy(updatedBy);

        PigLoss saved = pigLossRepository.save(entity);
        AuditContext.registerUpdated(saved);
        return pigLossMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "PIG_LOSS")
    public void delete(UUID id, UUID deletedBy) {
        PigLoss entity = pigLossRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigLoss", id.toString()));

        AuditContext.registerDeleted(entity);

        entity.setIsDeleted(true);
        entity.setDeletedAt(Instant.now());
        entity.setDeletedBy(deletedBy);
        pigLossRepository.save(entity);
    }
}
