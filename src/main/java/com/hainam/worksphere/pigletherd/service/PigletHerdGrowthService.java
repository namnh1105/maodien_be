package com.hainam.worksphere.pigletherd.service;

import com.hainam.worksphere.pigletherd.domain.PigletHerdGrowth;
import com.hainam.worksphere.pigletherd.dto.request.CreatePigletHerdGrowthRequest;
import com.hainam.worksphere.pigletherd.dto.request.UpdatePigletHerdGrowthRequest;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdGrowthResponse;
import com.hainam.worksphere.pigletherd.mapper.PigletHerdGrowthMapper;
import com.hainam.worksphere.pigletherd.repository.PigletHerdGrowthRepository;
import com.hainam.worksphere.pigletherd.repository.PigletHerdRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
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
public class PigletHerdGrowthService {

    private final PigletHerdGrowthRepository pigletHerdGrowthRepository;
    private final PigletHerdRepository pigletHerdRepository;
    private final PigletHerdGrowthMapper pigletHerdGrowthMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIGLET_HERD_GROWTH")
    public PigletHerdGrowthResponse create(CreatePigletHerdGrowthRequest request, UUID createdBy) {
        ensureHerdExists(request.getHerdId());

        PigletHerdGrowth growth = PigletHerdGrowth.builder()
                .herdId(request.getHerdId())
                .trackingDate(request.getTrackingDate())
                .averageWeight(request.getAverageWeight())
                .note(request.getNote())
                .createdBy(createdBy)
                .build();

        PigletHerdGrowth saved = pigletHerdGrowthRepository.save(growth);
        AuditContext.registerCreated(saved);
        return pigletHerdGrowthMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PigletHerdGrowthResponse> getAll() {
        return pigletHerdGrowthRepository.findAllActive().stream().map(pigletHerdGrowthMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PigletHerdGrowthResponse> getByHerdId(UUID herdId) {
        return pigletHerdGrowthRepository.findActiveByHerdId(herdId).stream().map(pigletHerdGrowthMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PigletHerdGrowthResponse getById(UUID id) {
        PigletHerdGrowth growth = pigletHerdGrowthRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigletHerdGrowth", id.toString()));
        return pigletHerdGrowthMapper.toResponse(growth);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PIGLET_HERD_GROWTH")
    public PigletHerdGrowthResponse update(UUID id, UpdatePigletHerdGrowthRequest request, UUID updatedBy) {
        PigletHerdGrowth growth = pigletHerdGrowthRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigletHerdGrowth", id.toString()));

        AuditContext.snapshot(growth);

        if (request.getTrackingDate() != null) growth.setTrackingDate(request.getTrackingDate());
        if (request.getAverageWeight() != null) growth.setAverageWeight(request.getAverageWeight());
        if (request.getNote() != null) growth.setNote(request.getNote());
        growth.setUpdatedBy(updatedBy);

        PigletHerdGrowth saved = pigletHerdGrowthRepository.save(growth);
        AuditContext.registerUpdated(saved);
        return pigletHerdGrowthMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "PIGLET_HERD_GROWTH")
    public void delete(UUID id, UUID deletedBy) {
        PigletHerdGrowth growth = pigletHerdGrowthRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigletHerdGrowth", id.toString()));

        AuditContext.registerDeleted(growth);

        growth.setIsDeleted(true);
        growth.setDeletedAt(Instant.now());
        growth.setDeletedBy(deletedBy);
        pigletHerdGrowthRepository.save(growth);
    }

    private void ensureHerdExists(UUID herdId) {
        pigletHerdRepository.findActiveById(herdId)
                .orElseThrow(() -> PigletHerdNotFoundException.byId(herdId.toString()));
    }
}
