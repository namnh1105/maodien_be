package com.hainam.worksphere.sale.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.sale.dto.request.CreateSaleRequest;
import com.hainam.worksphere.sale.dto.request.UpdateSaleRequest;
import com.hainam.worksphere.sale.dto.response.SaleResponse;
import com.hainam.worksphere.sale.service.SaleService;
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
@RequestMapping("/api/v1/sales")
@RequiredArgsConstructor
@Tag(name = "Sale Management")
@SecurityRequirement(name = "Bearer Authentication")
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    @Operation(summary = "Create sale")
    @RequirePermission(PermissionType.CREATE_SALE)
    public ResponseEntity<ApiResponse<SaleResponse>> create(
            @Valid @RequestBody CreateSaleRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        SaleResponse response = saleService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Sale created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all sales")
    @RequirePermission(PermissionType.VIEW_SALE)
    public ResponseEntity<ApiResponse<List<SaleResponse>>> getAll(
            @RequestParam(required = false) UUID customerId,
            @RequestParam(required = false) UUID pigId
    ) {
        if (customerId != null) {
            return ResponseEntity.ok(ApiResponse.success(saleService.getByCustomerId(customerId)));
        }
        if (pigId != null) {
            return ResponseEntity.ok(ApiResponse.success(saleService.getByPigId(pigId)));
        }
        return ResponseEntity.ok(ApiResponse.success(saleService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get sale by id")
    @RequirePermission(PermissionType.VIEW_SALE)
    public ResponseEntity<ApiResponse<SaleResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(saleService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update sale")
    @RequirePermission(PermissionType.UPDATE_SALE)
    public ResponseEntity<ApiResponse<SaleResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSaleRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        SaleResponse response = saleService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Sale updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sale")
    @RequirePermission(PermissionType.DELETE_SALE)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        saleService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Sale deleted successfully", null));
    }
}
