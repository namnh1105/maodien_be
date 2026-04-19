package com.hainam.worksphere.materialreceiptdetail.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.materialreceiptdetail.dto.request.CreateMaterialReceiptDetailRequest;
import com.hainam.worksphere.materialreceiptdetail.dto.request.UpdateMaterialReceiptDetailRequest;
import com.hainam.worksphere.materialreceiptdetail.dto.response.MaterialReceiptDetailResponse;
import com.hainam.worksphere.materialreceiptdetail.service.MaterialReceiptDetailService;
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
@RequestMapping("/api/v1/material-receipt-details")
@RequiredArgsConstructor
@Tag(name = "MaterialReceiptDetail Management")
@SecurityRequirement(name = "Bearer Authentication")
public class MaterialReceiptDetailController {

    private final MaterialReceiptDetailService materialReceiptDetailService;

    @PostMapping
    @Operation(summary = "Create material receipt detail")
    public ResponseEntity<ApiResponse<MaterialReceiptDetailResponse>> create(
            @Valid @RequestBody CreateMaterialReceiptDetailRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MaterialReceiptDetailResponse response = materialReceiptDetailService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Material receipt detail created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all material receipt details")
    public ResponseEntity<ApiResponse<List<MaterialReceiptDetailResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(materialReceiptDetailService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get material receipt detail by id")
    public ResponseEntity<ApiResponse<MaterialReceiptDetailResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(materialReceiptDetailService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update material receipt detail")
    public ResponseEntity<ApiResponse<MaterialReceiptDetailResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMaterialReceiptDetailRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MaterialReceiptDetailResponse response = materialReceiptDetailService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Material receipt detail updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete material receipt detail")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        materialReceiptDetailService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Material receipt detail deleted successfully", null));
    }
}
