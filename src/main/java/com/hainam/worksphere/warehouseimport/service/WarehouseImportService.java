package com.hainam.worksphere.warehouseimport.service;

import com.hainam.worksphere.feed.repository.FeedRepository;
import com.hainam.worksphere.livestockmaterial.repository.LivestockMaterialRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.SupplierNotFoundException;
import com.hainam.worksphere.shared.exception.VaccineNotFoundException;
import com.hainam.worksphere.shared.exception.WarehouseImportNotFoundException;
import com.hainam.worksphere.shared.exception.WarehouseNotFoundException;
import com.hainam.worksphere.supplier.domain.Supplier;
import com.hainam.worksphere.supplier.repository.SupplierRepository;
import com.hainam.worksphere.vaccine.repository.VaccineRepository;
import com.hainam.worksphere.warehouse.domain.Warehouse;
import com.hainam.worksphere.warehouse.repository.WarehouseRepository;
import com.hainam.worksphere.warehouseimport.domain.ItemType;
import com.hainam.worksphere.warehouseimport.domain.WarehouseImport;
import com.hainam.worksphere.warehouseimport.dto.request.CreateWarehouseImportRequest;
import com.hainam.worksphere.warehouseimport.dto.request.UpdateWarehouseImportRequest;
import com.hainam.worksphere.warehouseimport.dto.response.WarehouseImportResponse;
import com.hainam.worksphere.warehouseimport.mapper.WarehouseImportMapper;
import com.hainam.worksphere.warehouseimport.repository.WarehouseImportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseImportService {

    private final WarehouseImportRepository warehouseImportRepository;
    private final WarehouseRepository warehouseRepository;
    private final SupplierRepository supplierRepository;
    private final VaccineRepository vaccineRepository;
    private final FeedRepository feedRepository;
    private final LivestockMaterialRepository livestockMaterialRepository;
    private final WarehouseImportMapper warehouseImportMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "WAREHOUSE_IMPORT")
    public WarehouseImportResponse create(CreateWarehouseImportRequest request, UUID createdBy) {
        Warehouse warehouse = warehouseRepository.findActiveById(request.getWarehouseId())
                .orElseThrow(() -> WarehouseNotFoundException.byId(request.getWarehouseId().toString()));

        ItemType itemType = parseItemType(request.getItemType());
        validateItem(itemType, request.getItemId());

        WarehouseImport warehouseImport = WarehouseImport.builder()
                .warehouse(warehouse)
                .itemType(itemType)
                .itemId(request.getItemId())
                .quantity(request.getQuantity())
                .unit(request.getUnit())
                .importDate(request.getImportDate())
                .supplier(findSupplierOrNull(request.getSupplierId()))
                .createdBy(createdBy)
                .build();

        WarehouseImport saved = warehouseImportRepository.save(warehouseImport);
        AuditContext.registerCreated(saved);
        return warehouseImportMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<WarehouseImportResponse> getAll() {
        return warehouseImportRepository.findAllActive().stream().map(warehouseImportMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public WarehouseImportResponse getById(UUID id) {
        WarehouseImport warehouseImport = warehouseImportRepository.findActiveById(id)
                .orElseThrow(() -> WarehouseImportNotFoundException.byId(id.toString()));
        return warehouseImportMapper.toResponse(warehouseImport);
    }

    @Transactional(readOnly = true)
    public List<WarehouseImportResponse> getByWarehouseId(UUID warehouseId) {
        return warehouseImportRepository.findActiveByWarehouseId(warehouseId)
                .stream().map(warehouseImportMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<WarehouseImportResponse> getByItemType(String itemType) {
        ItemType parsed = parseItemType(itemType);
        return warehouseImportRepository.findActiveByItemType(parsed)
                .stream().map(warehouseImportMapper::toResponse).toList();
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "WAREHOUSE_IMPORT")
    public WarehouseImportResponse update(UUID id, UpdateWarehouseImportRequest request, UUID updatedBy) {
        WarehouseImport warehouseImport = warehouseImportRepository.findActiveById(id)
                .orElseThrow(() -> WarehouseImportNotFoundException.byId(id.toString()));

        AuditContext.snapshot(warehouseImport);

        if (request.getWarehouseId() != null) {
            Warehouse warehouse = warehouseRepository.findActiveById(request.getWarehouseId())
                    .orElseThrow(() -> WarehouseNotFoundException.byId(request.getWarehouseId().toString()));
            warehouseImport.setWarehouse(warehouse);
        }

        ItemType effectiveType = warehouseImport.getItemType();
        if (request.getItemType() != null) {
            effectiveType = parseItemType(request.getItemType());
            warehouseImport.setItemType(effectiveType);
        }

        if (request.getItemId() != null) {
            validateItem(effectiveType, request.getItemId());
            warehouseImport.setItemId(request.getItemId());
        }

        if (request.getQuantity() != null) warehouseImport.setQuantity(request.getQuantity());
        if (request.getUnit() != null) warehouseImport.setUnit(request.getUnit());
        if (request.getImportDate() != null) warehouseImport.setImportDate(request.getImportDate());
        if (request.getSupplierId() != null) warehouseImport.setSupplier(findSupplierOrNull(request.getSupplierId()));
        warehouseImport.setUpdatedBy(updatedBy);

        WarehouseImport saved = warehouseImportRepository.save(warehouseImport);
        AuditContext.registerUpdated(saved);
        return warehouseImportMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "WAREHOUSE_IMPORT")
    public void delete(UUID id, UUID deletedBy) {
        WarehouseImport warehouseImport = warehouseImportRepository.findActiveById(id)
                .orElseThrow(() -> WarehouseImportNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(warehouseImport);

        warehouseImport.setIsDeleted(true);
        warehouseImport.setDeletedAt(Instant.now());
        warehouseImport.setDeletedBy(deletedBy);
        warehouseImportRepository.save(warehouseImport);
    }

    private Supplier findSupplierOrNull(UUID supplierId) {
        if (supplierId == null) {
            return null;
        }
        return supplierRepository.findActiveById(supplierId)
                .orElseThrow(() -> SupplierNotFoundException.byId(supplierId.toString()));
    }

    private ItemType parseItemType(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            throw new BusinessRuleViolationException("Item type is required");
        }
        try {
            return ItemType.valueOf(rawType.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleViolationException("Invalid item type: " + rawType);
        }
    }

    private void validateItem(ItemType itemType, UUID itemId) {
        boolean exists = switch (itemType) {
            case VACCINE -> vaccineRepository.findActiveById(itemId).isPresent();
            case MATERIAL -> livestockMaterialRepository.findActiveById(itemId).isPresent();
            case FEED -> feedRepository.findActiveById(itemId).isPresent();
        };

        if (!exists) {
            if (itemType == ItemType.VACCINE) {
                throw VaccineNotFoundException.byId(itemId.toString());
            }
            throw new BusinessRuleViolationException("Item not found for type " + itemType + " with id: " + itemId);
        }
    }
}
