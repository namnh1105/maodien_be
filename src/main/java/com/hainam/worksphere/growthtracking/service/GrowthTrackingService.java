package com.hainam.worksphere.growthtracking.service;

import com.hainam.worksphere.growthtracking.domain.GrowthTracking;
import com.hainam.worksphere.growthtracking.dto.request.CreateGrowthTrackingRequest;
import com.hainam.worksphere.growthtracking.dto.request.UpdateGrowthTrackingRequest;
import com.hainam.worksphere.growthtracking.dto.response.GrowthTrackingResponse;
import com.hainam.worksphere.growthtracking.mapper.GrowthTrackingMapper;
import com.hainam.worksphere.growthtracking.repository.GrowthTrackingRepository;
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
public class GrowthTrackingService {

    private final GrowthTrackingRepository growthTrackingRepository;
    private final GrowthTrackingMapper growthTrackingMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "GROWTH_TRACKING")
    public GrowthTrackingResponse create(CreateGrowthTrackingRequest request, UUID createdBy) {
        GrowthTracking tracking = GrowthTracking.builder()
                .pigId(request.getPigId())
                .trackingDate(request.getTrackingDate())
                .litterLength(request.getLitterLength())
                .chestGirth(request.getChestGirth())
                .weight(request.getWeight())
                .growthRate(request.getGrowthRate())
                .adg(request.getAdg())
                .fcr(request.getFcr())
                .note(request.getNote())
                .createdBy(createdBy)
                .build();

        GrowthTracking saved = growthTrackingRepository.save(tracking);
        AuditContext.registerCreated(saved);
        return growthTrackingMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<GrowthTrackingResponse> getAll() {
        return growthTrackingRepository.findAllActive().stream().map(growthTrackingMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public GrowthTrackingResponse getById(UUID id) {
        GrowthTracking tracking = growthTrackingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrowthTracking", id.toString()));
        return growthTrackingMapper.toResponse(tracking);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "GROWTH_TRACKING")
    public GrowthTrackingResponse update(UUID id, UpdateGrowthTrackingRequest request, UUID updatedBy) {
        GrowthTracking tracking = growthTrackingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrowthTracking", id.toString()));

        AuditContext.snapshot(tracking);

        if (request.getPigId() != null) tracking.setPigId(request.getPigId());
        if (request.getTrackingDate() != null) tracking.setTrackingDate(request.getTrackingDate());
        if (request.getLitterLength() != null) tracking.setLitterLength(request.getLitterLength());
        if (request.getChestGirth() != null) tracking.setChestGirth(request.getChestGirth());
        if (request.getWeight() != null) tracking.setWeight(request.getWeight());
        if (request.getGrowthRate() != null) tracking.setGrowthRate(request.getGrowthRate());
        if (request.getAdg() != null) tracking.setAdg(request.getAdg());
        if (request.getFcr() != null) tracking.setFcr(request.getFcr());
        if (request.getNote() != null) tracking.setNote(request.getNote());
        tracking.setUpdatedBy(updatedBy);

        GrowthTracking saved = growthTrackingRepository.save(tracking);
        AuditContext.registerUpdated(saved);
        return growthTrackingMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "GROWTH_TRACKING")
    public void delete(UUID id, UUID deletedBy) {
        GrowthTracking tracking = growthTrackingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrowthTracking", id.toString()));

        AuditContext.registerDeleted(tracking);

        tracking.setIsDeleted(true);
        tracking.setDeletedAt(Instant.now());
        tracking.setDeletedBy(deletedBy);
        growthTrackingRepository.save(tracking);
    }
}
