package com.hainam.worksphere.supplier.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.shared.constant.PermissionType;
import com.hainam.worksphere.shared.dto.ApiResponse;
import com.hainam.worksphere.supplier.dto.request.CreateSupplierRequest;
import com.hainam.worksphere.supplier.dto.request.UpdateSupplierRequest;
import com.hainam.worksphere.supplier.dto.response.SupplierResponse;
import com.hainam.worksphere.supplier.service.SupplierService;
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
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
@Tag(name = "Supplier Management")
@SecurityRequirement(name = "Bearer Authentication")
public class SupplierController {

    private final SupplierService supplierService;

    @PostMapping
    @Operation(summary = "Create supplier")
    @RequirePermission(PermissionType.CREATE_SUPPLIER)
    public ResponseEntity<ApiResponse<SupplierResponse>> create(
            @Valid @RequestBody CreateSupplierRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        SupplierResponse response = supplierService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Supplier created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all suppliers")
    @RequirePermission(PermissionType.VIEW_SUPPLIER)
    public ResponseEntity<ApiResponse<List<SupplierResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(supplierService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get supplier by id")
    @RequirePermission(PermissionType.VIEW_SUPPLIER)
    public ResponseEntity<ApiResponse<SupplierResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(supplierService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update supplier")
    @RequirePermission(PermissionType.UPDATE_SUPPLIER)
    public ResponseEntity<ApiResponse<SupplierResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSupplierRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        SupplierResponse response = supplierService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Supplier updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete supplier")
    @RequirePermission(PermissionType.DELETE_SUPPLIER)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        supplierService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Supplier deleted successfully", null));
    }
}
