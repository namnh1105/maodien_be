package com.hainam.worksphere.pigimport.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.pigimport.dto.request.CreatePigImportInvoiceRequest;
import com.hainam.worksphere.pigimport.dto.response.PigImportInvoiceResponse;
import com.hainam.worksphere.pigimport.service.PigImportInvoiceService;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pig-import-invoices")
@RequiredArgsConstructor
@Tag(name = "Pig Import Invoice Management")
@SecurityRequirement(name = "Bearer Authentication")
public class PigImportInvoiceController {

    private final PigImportInvoiceService pigImportInvoiceService;

    @PostMapping
    @Operation(summary = "Create pig import invoice and imported pigs")
    @RequirePermission(PermissionType.CREATE_PIG)
    public ResponseEntity<ApiResponse<PigImportInvoiceResponse>> create(
            @Valid @RequestBody CreatePigImportInvoiceRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigImportInvoiceResponse response = pigImportInvoiceService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pig import invoice created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all pig import invoices")
    @RequirePermission(PermissionType.VIEW_PIG)
    public ResponseEntity<ApiResponse<List<PigImportInvoiceResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(pigImportInvoiceService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pig import invoice by id")
    @RequirePermission(PermissionType.VIEW_PIG)
    public ResponseEntity<ApiResponse<PigImportInvoiceResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(pigImportInvoiceService.getById(id)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pig import invoice")
    @RequirePermission(PermissionType.DELETE_PIG)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        pigImportInvoiceService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Pig import invoice deleted successfully", null));
    }
}
