package com.hainam.worksphere.materialissue.service;

import com.hainam.worksphere.materialissue.domain.MaterialIssue;
import com.hainam.worksphere.materialissue.dto.request.CreateMaterialIssueRequest;
import com.hainam.worksphere.materialissue.dto.request.UpdateMaterialIssueRequest;
import com.hainam.worksphere.materialissue.dto.response.MaterialIssueResponse;
import com.hainam.worksphere.materialissue.mapper.MaterialIssueMapper;
import com.hainam.worksphere.materialissue.repository.MaterialIssueRepository;
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
public class MaterialIssueService {

    private final MaterialIssueRepository materialIssueRepository;
    private final MaterialIssueMapper materialIssueMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "MATERIAL_ISSUE")
    public MaterialIssueResponse create(CreateMaterialIssueRequest request, UUID createdBy) {
        MaterialIssue materialIssue = materialIssueMapper.toEntity(request);
        materialIssue.setCreatedBy(createdBy);

        MaterialIssue saved = materialIssueRepository.save(materialIssue);
        AuditContext.registerCreated(saved);
        return materialIssueMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MaterialIssueResponse> getAll() {
        return materialIssueRepository.findAllActive().stream().map(materialIssueMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MaterialIssueResponse getById(UUID id) {
        MaterialIssue materialIssue = materialIssueRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialIssue", id));
        return materialIssueMapper.toResponse(materialIssue);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "MATERIAL_ISSUE")
    public MaterialIssueResponse update(UUID id, UpdateMaterialIssueRequest request, UUID updatedBy) {
        MaterialIssue materialIssue = materialIssueRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialIssue", id));

        AuditContext.snapshot(materialIssue);
        materialIssueMapper.updateEntityFromRequest(request, materialIssue);
        materialIssue.setUpdatedBy(updatedBy);

        MaterialIssue saved = materialIssueRepository.save(materialIssue);
        AuditContext.registerUpdated(saved);
        return materialIssueMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "MATERIAL_ISSUE")
    public void delete(UUID id, UUID deletedBy) {
        MaterialIssue materialIssue = materialIssueRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialIssue", id));

        AuditContext.registerDeleted(materialIssue);
        materialIssue.setIsDeleted(true);
        materialIssue.setDeletedAt(Instant.now());
        materialIssue.setDeletedBy(deletedBy);
        materialIssueRepository.save(materialIssue);
    }
}
