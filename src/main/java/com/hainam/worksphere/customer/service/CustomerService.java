package com.hainam.worksphere.customer.service;

import com.hainam.worksphere.customer.domain.Customer;
import com.hainam.worksphere.customer.domain.CustomerType;
import com.hainam.worksphere.customer.dto.request.CreateCustomerRequest;
import com.hainam.worksphere.customer.dto.request.UpdateCustomerRequest;
import com.hainam.worksphere.customer.dto.response.CustomerResponse;
import com.hainam.worksphere.customer.mapper.CustomerMapper;
import com.hainam.worksphere.customer.repository.CustomerRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.CustomerNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "CUSTOMER")
    public CustomerResponse create(CreateCustomerRequest request, UUID createdBy) {
        Customer customer = Customer.builder()
                .name(request.getName())
                .address(request.getAddress())
                .phone(request.getPhone())
                .email(request.getEmail())
                .customerType(parseCustomerType(request.getCustomerType()))
                .createdBy(createdBy)
                .build();

        Customer saved = customerRepository.save(customer);
        AuditContext.registerCreated(saved);
        return customerMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CustomerResponse> getAll() {
        return customerRepository.findAllActive().stream().map(customerMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public CustomerResponse getById(UUID id) {
        Customer customer = customerRepository.findActiveById(id)
                .orElseThrow(() -> CustomerNotFoundException.byId(id.toString()));
        return customerMapper.toResponse(customer);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "CUSTOMER")
    public CustomerResponse update(UUID id, UpdateCustomerRequest request, UUID updatedBy) {
        Customer customer = customerRepository.findActiveById(id)
                .orElseThrow(() -> CustomerNotFoundException.byId(id.toString()));

        AuditContext.snapshot(customer);

        if (request.getName() != null) customer.setName(request.getName());
        if (request.getAddress() != null) customer.setAddress(request.getAddress());
        if (request.getPhone() != null) customer.setPhone(request.getPhone());
        if (request.getEmail() != null) customer.setEmail(request.getEmail());
        if (request.getCustomerType() != null) customer.setCustomerType(parseCustomerType(request.getCustomerType()));
        customer.setUpdatedBy(updatedBy);

        Customer saved = customerRepository.save(customer);
        AuditContext.registerUpdated(saved);
        return customerMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "CUSTOMER")
    public void delete(UUID id, UUID deletedBy) {
        Customer customer = customerRepository.findActiveById(id)
                .orElseThrow(() -> CustomerNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(customer);

        customer.setIsDeleted(true);
        customer.setDeletedAt(Instant.now());
        customer.setDeletedBy(deletedBy);
        customerRepository.save(customer);
    }

    private CustomerType parseCustomerType(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            return CustomerType.INDIVIDUAL;
        }
        try {
            return CustomerType.valueOf(rawType.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleViolationException("Invalid customer type: " + rawType);
        }
    }
}
