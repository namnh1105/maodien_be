package com.hainam.worksphere.pigsemen.service;

import com.hainam.worksphere.pigsemen.domain.PigSemen;
import com.hainam.worksphere.pigsemen.dto.request.CreatePigSemenRequest;
import com.hainam.worksphere.pigsemen.dto.response.PigSemenResponse;
import com.hainam.worksphere.pigsemen.mapper.PigSemenMapper;
import com.hainam.worksphere.pigsemen.repository.PigSemenRepository;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PigSemenService {

    private final PigSemenRepository pigSemenRepository;
    private final PigSemenMapper pigSemenMapper;
    private final PigRepository pigRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIG_SEMEN")
    public PigSemenResponse create(CreatePigSemenRequest request, UUID createdBy) {
        PigSemen entity = PigSemen.builder()
                .boarPigId(request.getBoarPigId())
                .boarBreed(request.getBoarBreed())
                .collectionDate(request.getCollectionDate())
                .volume(request.getVolume())
                .motility(request.getMotility())
                .quality(request.getQuality())
                .status(request.getStatus())
                .note(request.getNote())
                .createdBy(createdBy)
                .build();

        PigSemen saved = pigSemenRepository.save(entity);
        AuditContext.registerCreated(saved);
        
        return toResponseWithEarTag(saved);
    }

    @Transactional(readOnly = true)
    public List<PigSemenResponse> getAll() {
        return pigSemenRepository.findAllActive().stream()
                .map(this::toResponseWithEarTag)
                .toList();
    }

    @Transactional(readOnly = true)
    public PigSemenResponse getById(UUID id) {
        PigSemen entity = pigSemenRepository.findActiveById(id)
                .orElseThrow(() -> new com.hainam.worksphere.shared.exception.ResourceNotFoundException("PigSemen", id.toString()));
        return toResponseWithEarTag(entity);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PIG_SEMEN")
    public PigSemenResponse update(UUID id, com.hainam.worksphere.pigsemen.dto.request.UpdatePigSemenRequest request, UUID updatedBy) {
        PigSemen entity = pigSemenRepository.findActiveById(id)
                .orElseThrow(() -> new com.hainam.worksphere.shared.exception.ResourceNotFoundException("PigSemen", id.toString()));

        AuditContext.snapshot(entity);

        if (request.getBoarPigId() != null) entity.setBoarPigId(request.getBoarPigId());
        if (request.getBoarBreed() != null) entity.setBoarBreed(request.getBoarBreed());
        if (request.getCollectionDate() != null) entity.setCollectionDate(request.getCollectionDate());
        if (request.getVolume() != null) entity.setVolume(request.getVolume());
        if (request.getMotility() != null) entity.setMotility(request.getMotility());
        if (request.getQuality() != null) entity.setQuality(request.getQuality());
        if (request.getStatus() != null) entity.setStatus(request.getStatus());
        if (request.getNote() != null) entity.setNote(request.getNote());
        entity.setUpdatedBy(updatedBy);

        PigSemen saved = pigSemenRepository.save(entity);
        AuditContext.registerUpdated(saved);

        return toResponseWithEarTag(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "PIG_SEMEN")
    public void delete(UUID id, UUID deletedBy) {
        PigSemen entity = pigSemenRepository.findActiveById(id)
                .orElseThrow(() -> new com.hainam.worksphere.shared.exception.ResourceNotFoundException("PigSemen", id.toString()));

        AuditContext.registerDeleted(entity);

        entity.setIsDeleted(true);
        entity.setDeletedAt(java.time.Instant.now());
        entity.setDeletedBy(deletedBy);
        pigSemenRepository.save(entity);
    }

    private PigSemenResponse toResponseWithEarTag(PigSemen pigSemen) {
        PigSemenResponse response = pigSemenMapper.toResponse(pigSemen);
        if (pigSemen.getBoarPigId() != null) {
            pigRepository.findActiveById(pigSemen.getBoarPigId())
                    .ifPresent(pig -> response.setBoarPigEarTag(pig.getEarTag()));
        }
        return response;
    }
}
