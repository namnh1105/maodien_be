package com.hainam.worksphere.livestockmaterial.service;

import com.hainam.worksphere.livestockmaterial.domain.LivestockMaterial;
import com.hainam.worksphere.livestockmaterial.dto.request.CreateLivestockMaterialRequest;
import com.hainam.worksphere.livestockmaterial.dto.request.UpdateLivestockMaterialRequest;
import com.hainam.worksphere.livestockmaterial.dto.response.LivestockMaterialResponse;
import com.hainam.worksphere.livestockmaterial.mapper.LivestockMaterialMapper;
import com.hainam.worksphere.livestockmaterial.repository.LivestockMaterialRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.LivestockMaterialNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivestockMaterialService {

    private final LivestockMaterialRepository livestockMaterialRepository;
    private final LivestockMaterialMapper livestockMaterialMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "LIVESTOCK_MATERIAL")
    public LivestockMaterialResponse create(CreateLivestockMaterialRequest request, UUID createdBy) {
        if (livestockMaterialRepository.existsActiveByMaterialCode(null)) {
            throw new BusinessRuleViolationException("Material code already exists: " + null);
        }

        LivestockMaterial material = LivestockMaterial.builder()
                .name(request.getName())
                .unit(request.getUnit())
                .createdBy(createdBy)
                .build();

        LivestockMaterial saved = livestockMaterialRepository.save(material);
        AuditContext.registerCreated(saved);
        return livestockMaterialMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<LivestockMaterialResponse> getAll() {
        return livestockMaterialRepository.findAllActive().stream().map(livestockMaterialMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public LivestockMaterialResponse getById(UUID id) {
        LivestockMaterial material = livestockMaterialRepository.findActiveById(id)
                .orElseThrow(() -> LivestockMaterialNotFoundException.byId(id.toString()));
        return livestockMaterialMapper.toResponse(material);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "LIVESTOCK_MATERIAL")
    public LivestockMaterialResponse update(UUID id, UpdateLivestockMaterialRequest request, UUID updatedBy) {
        LivestockMaterial material = livestockMaterialRepository.findActiveById(id)
                .orElseThrow(() -> LivestockMaterialNotFoundException.byId(id.toString()));

        AuditContext.snapshot(material);

        if (request.getName() != null) material.setName(request.getName());
        if (request.getUnit() != null) material.setUnit(request.getUnit());
        material.setUpdatedBy(updatedBy);

        LivestockMaterial saved = livestockMaterialRepository.save(material);
        AuditContext.registerUpdated(saved);
        return livestockMaterialMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "LIVESTOCK_MATERIAL")
    public void delete(UUID id, UUID deletedBy) {
        LivestockMaterial material = livestockMaterialRepository.findActiveById(id)
                .orElseThrow(() -> LivestockMaterialNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(material);

        material.setIsDeleted(true);
        material.setDeletedAt(Instant.now());
        material.setDeletedBy(deletedBy);
        livestockMaterialRepository.save(material);
    }
}
