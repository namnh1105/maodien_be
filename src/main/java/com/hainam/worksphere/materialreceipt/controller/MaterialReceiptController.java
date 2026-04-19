package com.hainam.worksphere.materialreceipt.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.materialreceipt.dto.request.CreateMaterialReceiptRequest;
import com.hainam.worksphere.materialreceipt.dto.request.UpdateMaterialReceiptRequest;
import com.hainam.worksphere.materialreceipt.dto.response.MaterialReceiptResponse;
import com.hainam.worksphere.materialreceipt.service.MaterialReceiptService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/material-receipts")
@RequiredArgsConstructor
@Tag(name = "MaterialReceipt Management")
@SecurityRequirement(name = "Bearer Authentication")
public class MaterialReceiptController {

    private final MaterialReceiptService materialReceiptService;

    @PostMapping
    @Operation(summary = "Create material receipt")
    public ResponseEntity<ApiResponse<MaterialReceiptResponse>> create(
            @Valid @RequestBody CreateMaterialReceiptRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MaterialReceiptResponse response = materialReceiptService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Material receipt created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all material receipts")
    public ResponseEntity<ApiResponse<List<MaterialReceiptResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(materialReceiptService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get material receipt by id")
    public ResponseEntity<ApiResponse<MaterialReceiptResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(materialReceiptService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update material receipt")
    public ResponseEntity<ApiResponse<MaterialReceiptResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMaterialReceiptRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MaterialReceiptResponse response = materialReceiptService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Material receipt updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete material receipt")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        materialReceiptService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Material receipt deleted successfully", null));
    }
}
