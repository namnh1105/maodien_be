package com.hainam.worksphere.customer.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.customer.dto.request.CreateCustomerRequest;
import com.hainam.worksphere.customer.dto.request.UpdateCustomerRequest;
import com.hainam.worksphere.customer.dto.response.CustomerResponse;
import com.hainam.worksphere.customer.service.CustomerService;
import com.hainam.worksphere.shared.constant.PermissionType;
import com.hainam.worksphere.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer Management")
@SecurityRequirement(name = "Bearer Authentication")
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    @Operation(summary = "Create customer")
    @RequirePermission(PermissionType.CREATE_CUSTOMER)
    public ResponseEntity<ApiResponse<CustomerResponse>> create(
            @Valid @RequestBody CreateCustomerRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        CustomerResponse response = customerService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Customer created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all customers")
    @RequirePermission(PermissionType.VIEW_CUSTOMER)
    public ResponseEntity<ApiResponse<List<CustomerResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(customerService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by id")
    @RequirePermission(PermissionType.VIEW_CUSTOMER)
    public ResponseEntity<ApiResponse<CustomerResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(customerService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    @RequirePermission(PermissionType.UPDATE_CUSTOMER)
    public ResponseEntity<ApiResponse<CustomerResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCustomerRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        CustomerResponse response = customerService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Customer updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer")
    @RequirePermission(PermissionType.DELETE_CUSTOMER)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        customerService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Customer deleted successfully", null));
    }
}
