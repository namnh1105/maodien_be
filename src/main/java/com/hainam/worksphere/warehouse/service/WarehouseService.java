package com.hainam.worksphere.warehouse.service;

import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.WarehouseNotFoundException;
import com.hainam.worksphere.warehouse.domain.Warehouse;
import com.hainam.worksphere.warehouse.domain.WarehouseType;
import com.hainam.worksphere.warehouse.dto.request.CreateWarehouseRequest;
import com.hainam.worksphere.warehouse.dto.request.UpdateWarehouseRequest;
import com.hainam.worksphere.warehouse.dto.response.WarehouseResponse;
import com.hainam.worksphere.warehouse.mapper.WarehouseMapper;
import com.hainam.worksphere.warehouse.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "WAREHOUSE")
    public WarehouseResponse create(CreateWarehouseRequest request, UUID createdBy) {
        Warehouse warehouse = Warehouse.builder()
                .name(request.getName())
                .warehouseType(parseWarehouseType(request.getWarehouseType()))
                .createdBy(createdBy)
                .build();

        Warehouse saved = warehouseRepository.save(warehouse);
        AuditContext.registerCreated(saved);
        return warehouseMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<WarehouseResponse> getAll() {
        return warehouseRepository.findAllActive().stream().map(warehouseMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public WarehouseResponse getById(UUID id) {
        Warehouse warehouse = warehouseRepository.findActiveById(id)
                .orElseThrow(() -> WarehouseNotFoundException.byId(id.toString()));
        return warehouseMapper.toResponse(warehouse);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "WAREHOUSE")
    public WarehouseResponse update(UUID id, UpdateWarehouseRequest request, UUID updatedBy) {
        Warehouse warehouse = warehouseRepository.findActiveById(id)
                .orElseThrow(() -> WarehouseNotFoundException.byId(id.toString()));

        AuditContext.snapshot(warehouse);

        if (request.getName() != null) warehouse.setName(request.getName());
        if (request.getWarehouseType() != null) warehouse.setWarehouseType(parseWarehouseType(request.getWarehouseType()));
        warehouse.setUpdatedBy(updatedBy);

        Warehouse saved = warehouseRepository.save(warehouse);
        AuditContext.registerUpdated(saved);
        return warehouseMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "WAREHOUSE")
    public void delete(UUID id, UUID deletedBy) {
        Warehouse warehouse = warehouseRepository.findActiveById(id)
                .orElseThrow(() -> WarehouseNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(warehouse);

        warehouse.setIsDeleted(true);
        warehouse.setDeletedAt(Instant.now());
        warehouse.setDeletedBy(deletedBy);
        warehouseRepository.save(warehouse);
    }

    private WarehouseType parseWarehouseType(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            return null;
        }
        try {
            return WarehouseType.valueOf(rawType.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleViolationException("Invalid warehouse type: " + rawType);
        }
    }
}
