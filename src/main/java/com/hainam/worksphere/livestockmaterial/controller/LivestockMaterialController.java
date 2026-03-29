package com.hainam.worksphere.livestockmaterial.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.livestockmaterial.dto.request.CreateLivestockMaterialRequest;
import com.hainam.worksphere.livestockmaterial.dto.request.UpdateLivestockMaterialRequest;
import com.hainam.worksphere.livestockmaterial.dto.response.LivestockMaterialResponse;
import com.hainam.worksphere.livestockmaterial.service.LivestockMaterialService;
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
@RequestMapping("/api/v1/livestock-materials")
@RequiredArgsConstructor
@Tag(name = "Livestock Material Management")
@SecurityRequirement(name = "Bearer Authentication")
public class LivestockMaterialController {

    private final LivestockMaterialService livestockMaterialService;

    @PostMapping
    @Operation(summary = "Create livestock material")
    @RequirePermission(PermissionType.CREATE_LIVESTOCK_MATERIAL)
    public ResponseEntity<ApiResponse<LivestockMaterialResponse>> create(
            @Valid @RequestBody CreateLivestockMaterialRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        LivestockMaterialResponse response = livestockMaterialService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Livestock material created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all livestock materials")
    @RequirePermission(PermissionType.VIEW_LIVESTOCK_MATERIAL)
    public ResponseEntity<ApiResponse<List<LivestockMaterialResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(livestockMaterialService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get livestock material by id")
    @RequirePermission(PermissionType.VIEW_LIVESTOCK_MATERIAL)
    public ResponseEntity<ApiResponse<LivestockMaterialResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(livestockMaterialService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update livestock material")
    @RequirePermission(PermissionType.UPDATE_LIVESTOCK_MATERIAL)
    public ResponseEntity<ApiResponse<LivestockMaterialResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateLivestockMaterialRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        LivestockMaterialResponse response = livestockMaterialService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Livestock material updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete livestock material")
    @RequirePermission(PermissionType.DELETE_LIVESTOCK_MATERIAL)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        livestockMaterialService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Livestock material deleted successfully", null));
    }
}
