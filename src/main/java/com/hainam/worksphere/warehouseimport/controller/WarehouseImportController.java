package com.hainam.worksphere.warehouseimport.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.shared.constant.PermissionType;
import com.hainam.worksphere.shared.dto.ApiResponse;
import com.hainam.worksphere.warehouseimport.dto.request.CreateWarehouseImportRequest;
import com.hainam.worksphere.warehouseimport.dto.request.UpdateWarehouseImportRequest;
import com.hainam.worksphere.warehouseimport.dto.response.WarehouseImportResponse;
import com.hainam.worksphere.warehouseimport.service.WarehouseImportService;
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
@RequestMapping("/api/v1/warehouse-imports")
@RequiredArgsConstructor
@Tag(name = "Warehouse Import Management")
@SecurityRequirement(name = "Bearer Authentication")
public class WarehouseImportController {

    private final WarehouseImportService warehouseImportService;

    @PostMapping
    @Operation(summary = "Create warehouse import")
    @RequirePermission(PermissionType.CREATE_WAREHOUSE_IMPORT)
    public ResponseEntity<ApiResponse<WarehouseImportResponse>> create(
            @Valid @RequestBody CreateWarehouseImportRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        WarehouseImportResponse response = warehouseImportService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Warehouse import created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all warehouse imports")
    @RequirePermission(PermissionType.VIEW_WAREHOUSE_IMPORT)
    public ResponseEntity<ApiResponse<List<WarehouseImportResponse>>> getAll(
            @RequestParam(required = false) UUID warehouseId,
            @RequestParam(required = false) String itemType
    ) {
        if (warehouseId != null) {
            return ResponseEntity.ok(ApiResponse.success(warehouseImportService.getByWarehouseId(warehouseId)));
        }
        if (itemType != null && !itemType.isBlank()) {
            return ResponseEntity.ok(ApiResponse.success(warehouseImportService.getByItemType(itemType)));
        }
        return ResponseEntity.ok(ApiResponse.success(warehouseImportService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get warehouse import by id")
    @RequirePermission(PermissionType.VIEW_WAREHOUSE_IMPORT)
    public ResponseEntity<ApiResponse<WarehouseImportResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(warehouseImportService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update warehouse import")
    @RequirePermission(PermissionType.UPDATE_WAREHOUSE_IMPORT)
    public ResponseEntity<ApiResponse<WarehouseImportResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateWarehouseImportRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        WarehouseImportResponse response = warehouseImportService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Warehouse import updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete warehouse import")
    @RequirePermission(PermissionType.DELETE_WAREHOUSE_IMPORT)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        warehouseImportService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Warehouse import deleted successfully", null));
    }
}
