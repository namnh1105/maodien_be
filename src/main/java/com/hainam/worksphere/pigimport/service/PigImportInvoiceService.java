package com.hainam.worksphere.pigimport.service;

import com.hainam.worksphere.breed.domain.Breed;
import com.hainam.worksphere.breed.repository.BreedRepository;
import com.hainam.worksphere.pen.repository.PenRepository;
import com.hainam.worksphere.penpig.domain.PenPig;
import com.hainam.worksphere.penpig.repository.PenPigRepository;
import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.domain.PigStatus;
import com.hainam.worksphere.pig.domain.PigType;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.pigimport.domain.PigImportInvoice;
import com.hainam.worksphere.pigimport.domain.PigImportInvoiceDetail;
import com.hainam.worksphere.pigimport.domain.PigImportInvoiceDetailPig;
import com.hainam.worksphere.pigimport.dto.request.CreatePigImportInvoiceDetailPigRequest;
import com.hainam.worksphere.pigimport.dto.request.CreatePigImportInvoiceDetailRequest;
import com.hainam.worksphere.pigimport.dto.request.CreatePigImportInvoiceRequest;
import com.hainam.worksphere.pigimport.dto.response.PigImportInvoiceDetailPigResponse;
import com.hainam.worksphere.pigimport.dto.response.PigImportInvoiceDetailResponse;
import com.hainam.worksphere.pigimport.dto.response.PigImportInvoiceResponse;
import com.hainam.worksphere.pigimport.repository.PigImportInvoiceDetailPigRepository;
import com.hainam.worksphere.pigimport.repository.PigImportInvoiceDetailRepository;
import com.hainam.worksphere.pigimport.repository.PigImportInvoiceRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.DuplicateResourceException;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import com.hainam.worksphere.supplier.domain.Supplier;
import com.hainam.worksphere.supplier.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PigImportInvoiceService {

    private final PigImportInvoiceRepository invoiceRepository;
    private final PigImportInvoiceDetailRepository detailRepository;
    private final PigImportInvoiceDetailPigRepository detailPigRepository;
    private final SupplierRepository supplierRepository;
    private final BreedRepository breedRepository;
    private final PigRepository pigRepository;
    private final PenRepository penRepository;
    private final PenPigRepository penPigRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIG_IMPORT_INVOICE")
    public PigImportInvoiceResponse create(CreatePigImportInvoiceRequest request, UUID createdBy) {
        if (invoiceRepository.existsActiveByInvoiceCode(request.getInvoiceCode())) {
            throw new DuplicateResourceException("Pig import invoice code already exists: " + request.getInvoiceCode());
        }

        Supplier supplier = null;
        if (request.getSupplierId() != null) {
            supplier = supplierRepository.findActiveById(request.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier", request.getSupplierId()));
        }
        String supplierName = resolveSupplierName(supplier, request.getSupplierName());

        int totalQuantity = request.getDetails().stream()
                .mapToInt(CreatePigImportInvoiceDetailRequest::getQuantity)
                .sum();
        double totalAmount = request.getDetails().stream()
                .mapToDouble(detail -> detail.getQuantity() * detail.getUnitPrice())
                .sum();

        PigImportInvoice invoice = PigImportInvoice.builder()
                .invoiceCode(request.getInvoiceCode())
                .supplier(supplier)
                .supplierName(supplierName)
                .importDate(request.getImportDate())
                .totalQuantity(totalQuantity)
                .totalAmount(totalAmount)
                .createdBy(createdBy)
                .build();
        PigImportInvoice savedInvoice = invoiceRepository.save(invoice);
        AuditContext.registerCreated(savedInvoice);

        for (CreatePigImportInvoiceDetailRequest detailRequest : request.getDetails()) {
            createDetail(savedInvoice, detailRequest, supplierName, createdBy);
        }

        return toResponse(savedInvoice);
    }

    @Transactional(readOnly = true)
    public List<PigImportInvoiceResponse> getAll() {
        return invoiceRepository.findAllActive().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PigImportInvoiceResponse getById(UUID id) {
        PigImportInvoice invoice = invoiceRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigImportInvoice", id));
        return toResponse(invoice);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "PIG_IMPORT_INVOICE")
    public void delete(UUID id, UUID deletedBy) {
        PigImportInvoice invoice = invoiceRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigImportInvoice", id));
        AuditContext.registerDeleted(invoice);
        invoice.setIsDeleted(true);
        invoice.setDeletedAt(Instant.now());
        invoice.setDeletedBy(deletedBy);
        invoiceRepository.save(invoice);
    }

    private void createDetail(
            PigImportInvoice invoice,
            CreatePigImportInvoiceDetailRequest request,
            String supplierName,
            UUID createdBy
    ) {
        if (request.getPigs().size() != request.getQuantity()) {
            throw new BusinessRuleViolationException("Imported pig count must match detail quantity");
        }

        Breed breed = null;
        if (request.getBreedId() != null) {
            breed = breedRepository.findActiveById(request.getBreedId())
                    .orElseThrow(() -> new ResourceNotFoundException("Breed", request.getBreedId()));
        }
        String breedName = breed != null ? breed.getName() : request.getBreedName();
        String species = breed != null ? breed.getId().toString() : breedName;
        PigType type = parsePigType(request.getType());
        double lineTotal = request.getQuantity() * request.getUnitPrice();

        PigImportInvoiceDetail detail = PigImportInvoiceDetail.builder()
                .invoice(invoice)
                .breed(breed)
                .breedName(breedName)
                .pigType(type)
                .quantity(request.getQuantity())
                .unitPrice(request.getUnitPrice())
                .lineTotal(lineTotal)
                .createdBy(createdBy)
                .build();
        PigImportInvoiceDetail savedDetail = detailRepository.save(detail);
        AuditContext.registerCreated(savedDetail);

        for (CreatePigImportInvoiceDetailPigRequest pigRequest : request.getPigs()) {
            Pig pig = createPig(invoice, pigRequest, type, supplierName, species, createdBy);
            if (pigRequest.getPenId() != null) {
                createPenAssignment(invoice, pig, pigRequest.getPenId(), createdBy);
            }

            PigImportInvoiceDetailPig detailPig = PigImportInvoiceDetailPig.builder()
                    .detail(savedDetail)
                    .pig(pig)
                    .penId(pigRequest.getPenId())
                    .createdBy(createdBy)
                    .build();
            PigImportInvoiceDetailPig savedDetailPig = detailPigRepository.save(detailPig);
            AuditContext.registerCreated(savedDetailPig);
        }
    }

    private Pig createPig(
            PigImportInvoice invoice,
            CreatePigImportInvoiceDetailPigRequest request,
            PigType type,
            String supplierName,
            String species,
            UUID createdBy
    ) {
        if (request.getEarTag() != null && !request.getEarTag().isBlank()
                && pigRepository.findActiveByEarTag(request.getEarTag()).isPresent()) {
            throw new DuplicateResourceException("Pig ear tag already exists: " + request.getEarTag());
        }

        Pig pig = Pig.builder()
                .earTag(request.getEarTag())
                .birthWeight(request.getBirthWeight())
                .birthDate(request.getBirthDate())
                .type(type)
                .origin(supplierName)
                .species(species)
                .nippleCount(request.getNippleCount())
                .herdEntryDate(invoice.getImportDate())
                .status(PigStatus.ACTIVE)
                .createdBy(createdBy)
                .build();
        Pig savedPig = pigRepository.save(pig);
        AuditContext.registerCreated(savedPig);
        return savedPig;
    }

    private void createPenAssignment(PigImportInvoice invoice, Pig pig, UUID penId, UUID createdBy) {
        penRepository.findActiveById(penId)
                .orElseThrow(() -> new ResourceNotFoundException("Pen", penId));
        PenPig penPig = PenPig.builder()
                .penId(penId)
                .pigId(pig.getId())
                .entryDate(invoice.getImportDate())
                .status("ACTIVE")
                .createdBy(createdBy)
                .build();
        PenPig savedPenPig = penPigRepository.save(penPig);
        AuditContext.registerCreated(savedPenPig);
    }

    private PigImportInvoiceResponse toResponse(PigImportInvoice invoice) {
        List<PigImportInvoiceDetail> details = detailRepository.findActiveByInvoiceId(invoice.getId());
        List<UUID> detailIds = details.stream().map(PigImportInvoiceDetail::getId).toList();
        Map<UUID, List<PigImportInvoiceDetailPig>> detailPigMap = detailIds.isEmpty()
                ? Map.of()
                : detailPigRepository.findActiveByDetailIds(detailIds).stream()
                        .collect(Collectors.groupingBy(detailPig -> detailPig.getDetail().getId()));

        return PigImportInvoiceResponse.builder()
                .id(invoice.getId())
                .invoiceCode(invoice.getInvoiceCode())
                .supplierId(invoice.getSupplier() == null ? null : invoice.getSupplier().getId())
                .supplierName(invoice.getSupplierName())
                .importDate(invoice.getImportDate())
                .totalQuantity(invoice.getTotalQuantity())
                .totalAmount(invoice.getTotalAmount())
                .details(details.stream()
                        .map(detail -> toDetailResponse(detail, detailPigMap.getOrDefault(detail.getId(), List.of())))
                        .toList())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }

    private PigImportInvoiceDetailResponse toDetailResponse(
            PigImportInvoiceDetail detail,
            List<PigImportInvoiceDetailPig> detailPigs
    ) {
        return PigImportInvoiceDetailResponse.builder()
                .id(detail.getId())
                .invoiceId(detail.getInvoice().getId())
                .breedId(detail.getBreed() == null ? null : detail.getBreed().getId())
                .breedName(detail.getBreedName())
                .type(detail.getPigType())
                .quantity(detail.getQuantity())
                .unitPrice(detail.getUnitPrice())
                .lineTotal(detail.getLineTotal())
                .pigs(detailPigs.stream().map(this::toDetailPigResponse).toList())
                .createdAt(detail.getCreatedAt())
                .updatedAt(detail.getUpdatedAt())
                .build();
    }

    private PigImportInvoiceDetailPigResponse toDetailPigResponse(PigImportInvoiceDetailPig detailPig) {
        return PigImportInvoiceDetailPigResponse.builder()
                .id(detailPig.getId())
                .detailId(detailPig.getDetail().getId())
                .pigId(detailPig.getPig().getId())
                .earTag(detailPig.getPig().getEarTag())
                .penId(detailPig.getPenId())
                .build();
    }

    private String resolveSupplierName(Supplier supplier, String supplierName) {
        if (supplier != null) return supplier.getName();
        if (supplierName == null || supplierName.isBlank()) {
            throw new BusinessRuleViolationException("Supplier id or supplier name is required");
        }
        return supplierName;
    }

    private PigType parsePigType(String type) {
        try {
            return PigType.valueOf(type.trim().toUpperCase());
        } catch (Exception ex) {
            throw new BusinessRuleViolationException("Invalid pig type: " + type);
        }
    }
}
