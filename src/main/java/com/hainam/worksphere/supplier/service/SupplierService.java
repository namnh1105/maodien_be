package com.hainam.worksphere.supplier.service;

import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.SupplierNotFoundException;
import com.hainam.worksphere.supplier.domain.Supplier;
import com.hainam.worksphere.supplier.dto.request.CreateSupplierRequest;
import com.hainam.worksphere.supplier.dto.request.UpdateSupplierRequest;
import com.hainam.worksphere.supplier.dto.response.SupplierResponse;
import com.hainam.worksphere.supplier.mapper.SupplierMapper;
import com.hainam.worksphere.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "SUPPLIER")
    public SupplierResponse create(CreateSupplierRequest request, UUID createdBy) {
        if (supplierRepository.existsActiveBySupplierCode(request.getSupplierCode())) {
            throw new BusinessRuleViolationException("Supplier code already exists: " + request.getSupplierCode());
        }

        Supplier supplier = Supplier.builder()
                .supplierCode(request.getSupplierCode())
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .createdBy(createdBy)
                .build();

        Supplier saved = supplierRepository.save(supplier);
        AuditContext.registerCreated(saved);
        return supplierMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SupplierResponse> getAll() {
        return supplierRepository.findAllActive().stream().map(supplierMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SupplierResponse getById(UUID id) {
        Supplier supplier = supplierRepository.findActiveById(id)
                .orElseThrow(() -> SupplierNotFoundException.byId(id.toString()));
        return supplierMapper.toResponse(supplier);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "SUPPLIER")
    public SupplierResponse update(UUID id, UpdateSupplierRequest request, UUID updatedBy) {
        Supplier supplier = supplierRepository.findActiveById(id)
                .orElseThrow(() -> SupplierNotFoundException.byId(id.toString()));

        AuditContext.snapshot(supplier);

        if (request.getName() != null) supplier.setName(request.getName());
        if (request.getAddress() != null) supplier.setAddress(request.getAddress());
        if (request.getPhone() != null) supplier.setPhone(request.getPhone());
        if (request.getEmail() != null) supplier.setEmail(request.getEmail());
        supplier.setUpdatedBy(updatedBy);

        Supplier saved = supplierRepository.save(supplier);
        AuditContext.registerUpdated(saved);
        return supplierMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "SUPPLIER")
    public void delete(UUID id, UUID deletedBy) {
        Supplier supplier = supplierRepository.findActiveById(id)
                .orElseThrow(() -> SupplierNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(supplier);

        supplier.setIsDeleted(true);
        supplier.setDeletedAt(Instant.now());
        supplier.setDeletedBy(deletedBy);
        supplierRepository.save(supplier);
    }
}
