package com.hainam.worksphere.materialreceipt.service;

import com.hainam.worksphere.materialreceipt.domain.MaterialReceipt;
import com.hainam.worksphere.materialreceipt.dto.request.CreateMaterialReceiptRequest;
import com.hainam.worksphere.materialreceipt.dto.request.UpdateMaterialReceiptRequest;
import com.hainam.worksphere.materialreceipt.dto.response.MaterialReceiptResponse;
import com.hainam.worksphere.materialreceipt.mapper.MaterialReceiptMapper;
import com.hainam.worksphere.materialreceipt.repository.MaterialReceiptRepository;
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
public class MaterialReceiptService {

    private final MaterialReceiptRepository materialReceiptRepository;
    private final MaterialReceiptMapper materialReceiptMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "MATERIAL_RECEIPT")
    public MaterialReceiptResponse create(CreateMaterialReceiptRequest request, UUID createdBy) {
        MaterialReceipt materialReceipt = materialReceiptMapper.toEntity(request);
        materialReceipt.setCreatedBy(createdBy);

        MaterialReceipt saved = materialReceiptRepository.save(materialReceipt);
        AuditContext.registerCreated(saved);
        return materialReceiptMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MaterialReceiptResponse> getAll() {
        return materialReceiptRepository.findAllActive().stream().map(materialReceiptMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MaterialReceiptResponse getById(UUID id) {
        MaterialReceipt materialReceipt = materialReceiptRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialReceipt", id));
        return materialReceiptMapper.toResponse(materialReceipt);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "MATERIAL_RECEIPT")
    public MaterialReceiptResponse update(UUID id, UpdateMaterialReceiptRequest request, UUID updatedBy) {
        MaterialReceipt materialReceipt = materialReceiptRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialReceipt", id));

        AuditContext.snapshot(materialReceipt);
        materialReceiptMapper.updateEntityFromRequest(request, materialReceipt);
        materialReceipt.setUpdatedBy(updatedBy);

        MaterialReceipt saved = materialReceiptRepository.save(materialReceipt);
        AuditContext.registerUpdated(saved);
        return materialReceiptMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "MATERIAL_RECEIPT")
    public void delete(UUID id, UUID deletedBy) {
        MaterialReceipt materialReceipt = materialReceiptRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MaterialReceipt", id));

        AuditContext.registerDeleted(materialReceipt);
        materialReceipt.setIsDeleted(true);
        materialReceipt.setDeletedAt(Instant.now());
        materialReceipt.setDeletedBy(deletedBy);
        materialReceiptRepository.save(materialReceipt);
    }
}
