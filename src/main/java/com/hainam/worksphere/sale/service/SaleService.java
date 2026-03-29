package com.hainam.worksphere.sale.service;

import com.hainam.worksphere.customer.domain.Customer;
import com.hainam.worksphere.customer.repository.CustomerRepository;
import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.sale.domain.Sale;
import com.hainam.worksphere.sale.dto.request.CreateSaleRequest;
import com.hainam.worksphere.sale.dto.request.UpdateSaleRequest;
import com.hainam.worksphere.sale.dto.response.SaleResponse;
import com.hainam.worksphere.sale.mapper.SaleMapper;
import com.hainam.worksphere.sale.repository.SaleRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.CustomerNotFoundException;
import com.hainam.worksphere.shared.exception.PigNotFoundException;
import com.hainam.worksphere.shared.exception.SaleNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final PigRepository pigRepository;
    private final SaleMapper saleMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "SALE")
    public SaleResponse create(CreateSaleRequest request, UUID createdBy) {
        Customer customer = customerRepository.findActiveById(request.getCustomerId())
                .orElseThrow(() -> CustomerNotFoundException.byId(request.getCustomerId().toString()));

        Pig pig = pigRepository.findActiveById(request.getPigId())
                .orElseThrow(() -> PigNotFoundException.byId(request.getPigId().toString()));

        Sale sale = Sale.builder()
                .customer(customer)
                .pig(pig)
                .saleDate(request.getSaleDate())
                .price(request.getPrice())
                .note(request.getNote())
                .createdBy(createdBy)
                .build();

        Sale saved = saleRepository.save(sale);
        AuditContext.registerCreated(saved);
        return saleMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<SaleResponse> getAll() {
        return saleRepository.findAllActive().stream().map(saleMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SaleResponse getById(UUID id) {
        Sale sale = saleRepository.findActiveById(id)
                .orElseThrow(() -> SaleNotFoundException.byId(id.toString()));
        return saleMapper.toResponse(sale);
    }

    @Transactional(readOnly = true)
    public List<SaleResponse> getByCustomerId(UUID customerId) {
        return saleRepository.findActiveByCustomerId(customerId).stream().map(saleMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<SaleResponse> getByPigId(UUID pigId) {
        return saleRepository.findActiveByPigId(pigId).stream().map(saleMapper::toResponse).toList();
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "SALE")
    public SaleResponse update(UUID id, UpdateSaleRequest request, UUID updatedBy) {
        Sale sale = saleRepository.findActiveById(id)
                .orElseThrow(() -> SaleNotFoundException.byId(id.toString()));

        AuditContext.snapshot(sale);

        if (request.getCustomerId() != null) {
            Customer customer = customerRepository.findActiveById(request.getCustomerId())
                    .orElseThrow(() -> CustomerNotFoundException.byId(request.getCustomerId().toString()));
            sale.setCustomer(customer);
        }

        if (request.getPigId() != null) {
            Pig pig = pigRepository.findActiveById(request.getPigId())
                    .orElseThrow(() -> PigNotFoundException.byId(request.getPigId().toString()));
            sale.setPig(pig);
        }

        if (request.getSaleDate() != null) sale.setSaleDate(request.getSaleDate());
        if (request.getPrice() != null) sale.setPrice(request.getPrice());
        if (request.getNote() != null) sale.setNote(request.getNote());
        sale.setUpdatedBy(updatedBy);

        Sale saved = saleRepository.save(sale);
        AuditContext.registerUpdated(saved);
        return saleMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "SALE")
    public void delete(UUID id, UUID deletedBy) {
        Sale sale = saleRepository.findActiveById(id)
                .orElseThrow(() -> SaleNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(sale);

        sale.setIsDeleted(true);
        sale.setDeletedAt(Instant.now());
        sale.setDeletedBy(deletedBy);
        saleRepository.save(sale);
    }
}
