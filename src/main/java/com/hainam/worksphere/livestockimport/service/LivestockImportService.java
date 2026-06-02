package com.hainam.worksphere.livestockimport.service;

import com.hainam.worksphere.employee.repository.EmployeeRepository;
import com.hainam.worksphere.livestockimport.dto.request.CreateLivestockImportItemRequest;
import com.hainam.worksphere.livestockimport.dto.request.CreateLivestockImportRequest;
import com.hainam.worksphere.livestockimport.dto.response.LivestockImportItemResponse;
import com.hainam.worksphere.livestockimport.dto.response.LivestockImportResponse;
import com.hainam.worksphere.livestockmaterial.domain.LivestockMaterial;
import com.hainam.worksphere.livestockmaterial.repository.LivestockMaterialRepository;
import com.hainam.worksphere.materialreceipt.domain.MaterialReceipt;
import com.hainam.worksphere.materialreceipt.repository.MaterialReceiptRepository;
import com.hainam.worksphere.materialreceiptdetail.domain.MaterialReceiptDetail;
import com.hainam.worksphere.materialreceiptdetail.repository.MaterialReceiptDetailRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.LivestockMaterialNotFoundException;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import com.hainam.worksphere.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivestockImportService {

    private static final String ITEM_TYPE = "LIVESTOCK_MATERIAL";

    private final MaterialReceiptRepository materialReceiptRepository;
    private final MaterialReceiptDetailRepository materialReceiptDetailRepository;
    private final LivestockMaterialRepository livestockMaterialRepository;
    private final EmployeeRepository employeeRepository;
    private final SupplierRepository supplierRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "LIVESTOCK_IMPORT")
    public LivestockImportResponse create(CreateLivestockImportRequest request, UUID createdBy) {
        validateReferences(request);

        List<PreparedItem> preparedItems = request.getItems().stream()
                .map(this::prepareItem)
                .toList();

        double calculatedTotal = preparedItems.stream()
                .map(PreparedItem::lineTotal)
                .filter(value -> value != null)
                .mapToDouble(Double::doubleValue)
                .sum();

        MaterialReceipt receipt = MaterialReceipt.builder()
                .receiptDate(request.getReceiptDate())
                .employeeId(request.getEmployeeId())
                .supplierId(request.getSupplierId())
                .totalAmount(request.getTotalAmount() == null ? calculatedTotal : request.getTotalAmount())
                .createdBy(createdBy)
                .build();

        MaterialReceipt savedReceipt = materialReceiptRepository.save(receipt);
        AuditContext.registerCreated(savedReceipt);

        List<LivestockImportItemResponse> itemResponses = new ArrayList<>();
        for (PreparedItem preparedItem : preparedItems) {
            LivestockMaterial material = preparedItem.material();
            Double currentQuantity = material.getQuantity() == null ? 0D : material.getQuantity();

            AuditContext.snapshot(material);
            material.setQuantity(currentQuantity + preparedItem.request().getQuantity());
            material.setUpdatedBy(createdBy);
            LivestockMaterial savedMaterial = livestockMaterialRepository.save(material);
            AuditContext.registerUpdated(savedMaterial);

            MaterialReceiptDetail detail = MaterialReceiptDetail.builder()
                    .receiptId(savedReceipt.getId())
                    .itemType(ITEM_TYPE)
                    .itemId(savedMaterial.getId())
                    .quantity(preparedItem.request().getQuantity())
                    .unitPrice(preparedItem.request().getUnitPrice())
                    .lineTotal(preparedItem.lineTotal())
                    .createdBy(createdBy)
                    .build();
            MaterialReceiptDetail savedDetail = materialReceiptDetailRepository.save(detail);
            AuditContext.registerCreated(savedDetail);

            itemResponses.add(toItemResponse(savedDetail, savedMaterial));
        }

        return LivestockImportResponse.builder()
                .id(savedReceipt.getId())
                .receiptDate(savedReceipt.getReceiptDate())
                .employeeId(savedReceipt.getEmployeeId())
                .supplierId(savedReceipt.getSupplierId())
                .totalAmount(savedReceipt.getTotalAmount())
                .items(itemResponses)
                .createdAt(savedReceipt.getCreatedAt())
                .updatedAt(savedReceipt.getUpdatedAt())
                .build();
    }

    private void validateReferences(CreateLivestockImportRequest request) {
        employeeRepository.findActiveById(request.getEmployeeId())
                .orElseThrow(() -> new ResourceNotFoundException("Employee", request.getEmployeeId()));

        if (request.getSupplierId() != null) {
            supplierRepository.findActiveById(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getSupplierId()));
        }
    }

    private PreparedItem prepareItem(CreateLivestockImportItemRequest item) {
        LivestockMaterial material = livestockMaterialRepository.findActiveById(item.getMaterialId())
                .orElseThrow(() -> LivestockMaterialNotFoundException.byId(item.getMaterialId().toString()));
        Double lineTotal = item.getLineTotal();
        if (lineTotal == null && item.getUnitPrice() != null) {
            lineTotal = item.getUnitPrice() * item.getQuantity();
        }
        return new PreparedItem(item, material, lineTotal);
    }

    private LivestockImportItemResponse toItemResponse(MaterialReceiptDetail detail, LivestockMaterial material) {
        return LivestockImportItemResponse.builder()
                .id(detail.getId())
                .receiptId(detail.getReceiptId())
                .materialId(material.getId())
                .materialName(material.getName())
                .unit(material.getUnit())
                .quantity(detail.getQuantity())
                .unitPrice(detail.getUnitPrice())
                .lineTotal(detail.getLineTotal())
                .currentQuantity(material.getQuantity())
                .build();
    }

    private record PreparedItem(
            CreateLivestockImportItemRequest request,
            LivestockMaterial material,
            Double lineTotal
    ) {
    }
}
