package com.hainam.worksphere.warehouse.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.shared.constant.PermissionType;
import com.hainam.worksphere.shared.dto.ApiResponse;
import com.hainam.worksphere.warehouse.dto.request.CreateWarehouseRequest;
import com.hainam.worksphere.warehouse.dto.request.UpdateWarehouseRequest;
import com.hainam.worksphere.warehouse.dto.response.WarehouseResponse;
import com.hainam.worksphere.warehouse.service.WarehouseService;
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
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
@Tag(name = "Warehouse Management")
@SecurityRequirement(name = "Bearer Authentication")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PostMapping
    @Operation(summary = "Create warehouse")
    @RequirePermission(PermissionType.CREATE_WAREHOUSE)
    public ResponseEntity<ApiResponse<WarehouseResponse>> create(
            @Valid @RequestBody CreateWarehouseRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        WarehouseResponse response = warehouseService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Warehouse created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all warehouses")
    @RequirePermission(PermissionType.VIEW_WAREHOUSE)
    public ResponseEntity<ApiResponse<List<WarehouseResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(warehouseService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get warehouse by id")
    @RequirePermission(PermissionType.VIEW_WAREHOUSE)
    public ResponseEntity<ApiResponse<WarehouseResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(warehouseService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update warehouse")
    @RequirePermission(PermissionType.UPDATE_WAREHOUSE)
    public ResponseEntity<ApiResponse<WarehouseResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateWarehouseRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        WarehouseResponse response = warehouseService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Warehouse updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete warehouse")
    @RequirePermission(PermissionType.DELETE_WAREHOUSE)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        warehouseService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Warehouse deleted successfully", null));
    }
}
