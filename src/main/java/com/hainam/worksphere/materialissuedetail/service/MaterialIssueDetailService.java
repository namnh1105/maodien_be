package com.hainam.worksphere.materialissuedetail.service;

import com.hainam.worksphere.materialissuedetail.domain.MaterialIssueDetail;
import com.hainam.worksphere.materialissuedetail.dto.request.CreateMaterialIssueDetailRequest;
import com.hainam.worksphere.materialissuedetail.dto.request.UpdateMaterialIssueDetailRequest;
import com.hainam.worksphere.materialissuedetail.dto.response.MaterialIssueDetailResponse;
import com.hainam.worksphere.materialissuedetail.mapper.MaterialIssueDetailMapper;
import com.hainam.worksphere.materialissuedetail.repository.MaterialIssueDetailRepository;
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
public class MaterialIssueDetailService {

    private final MaterialIssueDetailRepository materialIssueDetailRepository;
    private final MaterialIssueDetailMapper materialIssueDetailMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "MATERIAL_ISSUE_DETAIL")
    public MaterialIssueDetailResponse create(CreateMaterialIssueDetailRequest request, UUID createdBy) {
        MaterialIssueDetail materialIssueDetail = materialIssueDetailMapper.toEntity(request);
        materialIssueDetail.setCreatedBy(createdBy);

        MaterialIssueDetail saved = materialIssueDetailRepository.save(materialIssueDetail);
        AuditContext.registerCreated(saved);
        return materialIssueDetailMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MaterialIssueDetailResponse> getAll() {
        return materialIssueDetailRepository.findAllActive().stream().map(materialIssueDetailMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MaterialIssueDetailResponse getById(UUID id) {
        MaterialIssueDetail materialIssueDetail = materialIssueDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialIssueDetail", id));
        return materialIssueDetailMapper.toResponse(materialIssueDetail);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "MATERIAL_ISSUE_DETAIL")
    public MaterialIssueDetailResponse update(UUID id, UpdateMaterialIssueDetailRequest request, UUID updatedBy) {
        MaterialIssueDetail materialIssueDetail = materialIssueDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialIssueDetail", id));

        AuditContext.snapshot(materialIssueDetail);
        materialIssueDetailMapper.updateEntityFromRequest(request, materialIssueDetail);
        materialIssueDetail.setUpdatedBy(updatedBy);

        MaterialIssueDetail saved = materialIssueDetailRepository.save(materialIssueDetail);
        AuditContext.registerUpdated(saved);
        return materialIssueDetailMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "MATERIAL_ISSUE_DETAIL")
    public void delete(UUID id, UUID deletedBy) {
        MaterialIssueDetail materialIssueDetail = materialIssueDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialIssueDetail", id));

        AuditContext.registerDeleted(materialIssueDetail);
        materialIssueDetail.setIsDeleted(true);
        materialIssueDetail.setDeletedAt(Instant.now());
        materialIssueDetail.setDeletedBy(deletedBy);
        materialIssueDetailRepository.save(materialIssueDetail);
    }
}
