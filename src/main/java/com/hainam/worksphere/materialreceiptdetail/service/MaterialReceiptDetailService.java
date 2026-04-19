package com.hainam.worksphere.materialreceiptdetail.service;

import com.hainam.worksphere.materialreceiptdetail.domain.MaterialReceiptDetail;
import com.hainam.worksphere.materialreceiptdetail.dto.request.CreateMaterialReceiptDetailRequest;
import com.hainam.worksphere.materialreceiptdetail.dto.request.UpdateMaterialReceiptDetailRequest;
import com.hainam.worksphere.materialreceiptdetail.dto.response.MaterialReceiptDetailResponse;
import com.hainam.worksphere.materialreceiptdetail.mapper.MaterialReceiptDetailMapper;
import com.hainam.worksphere.materialreceiptdetail.repository.MaterialReceiptDetailRepository;
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
public class MaterialReceiptDetailService {

    private final MaterialReceiptDetailRepository materialReceiptDetailRepository;
    private final MaterialReceiptDetailMapper materialReceiptDetailMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "MATERIAL_RECEIPT_DETAIL")
    public MaterialReceiptDetailResponse create(CreateMaterialReceiptDetailRequest request, UUID createdBy) {
        MaterialReceiptDetail materialReceiptDetail = materialReceiptDetailMapper.toEntity(request);
        materialReceiptDetail.setCreatedBy(createdBy);

        MaterialReceiptDetail saved = materialReceiptDetailRepository.save(materialReceiptDetail);
        AuditContext.registerCreated(saved);
        return materialReceiptDetailMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MaterialReceiptDetailResponse> getAll() {
        return materialReceiptDetailRepository.findAllActive().stream().map(materialReceiptDetailMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MaterialReceiptDetailResponse getById(UUID id) {
        MaterialReceiptDetail materialReceiptDetail = materialReceiptDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialReceiptDetail", id));
        return materialReceiptDetailMapper.toResponse(materialReceiptDetail);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "MATERIAL_RECEIPT_DETAIL")
    public MaterialReceiptDetailResponse update(UUID id, UpdateMaterialReceiptDetailRequest request, UUID updatedBy) {
        MaterialReceiptDetail materialReceiptDetail = materialReceiptDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialReceiptDetail", id));

        AuditContext.snapshot(materialReceiptDetail);
        materialReceiptDetailMapper.updateEntityFromRequest(request, materialReceiptDetail);
        materialReceiptDetail.setUpdatedBy(updatedBy);

        MaterialReceiptDetail saved = materialReceiptDetailRepository.save(materialReceiptDetail);
        AuditContext.registerUpdated(saved);
        return materialReceiptDetailMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "MATERIAL_RECEIPT_DETAIL")
    public void delete(UUID id, UUID deletedBy) {
        MaterialReceiptDetail materialReceiptDetail = materialReceiptDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialReceiptDetail", id));

        AuditContext.registerDeleted(materialReceiptDetail);
        materialReceiptDetail.setIsDeleted(true);
        materialReceiptDetail.setDeletedAt(Instant.now());
        materialReceiptDetail.setDeletedBy(deletedBy);
        materialReceiptDetailRepository.save(materialReceiptDetail);
    }
}
