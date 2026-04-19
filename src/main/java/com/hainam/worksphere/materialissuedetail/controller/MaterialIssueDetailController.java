package com.hainam.worksphere.materialissuedetail.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.materialissuedetail.dto.request.CreateMaterialIssueDetailRequest;
import com.hainam.worksphere.materialissuedetail.dto.request.UpdateMaterialIssueDetailRequest;
import com.hainam.worksphere.materialissuedetail.dto.response.MaterialIssueDetailResponse;
import com.hainam.worksphere.materialissuedetail.service.MaterialIssueDetailService;
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
@RequestMapping("/api/v1/material-issue-details")
@RequiredArgsConstructor
@Tag(name = "MaterialIssueDetail Management")
@SecurityRequirement(name = "Bearer Authentication")
public class MaterialIssueDetailController {

    private final MaterialIssueDetailService materialIssueDetailService;

    @PostMapping
    @Operation(summary = "Create material issue detail")
    public ResponseEntity<ApiResponse<MaterialIssueDetailResponse>> create(
            @Valid @RequestBody CreateMaterialIssueDetailRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MaterialIssueDetailResponse response = materialIssueDetailService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Material issue detail created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all material issue details")
    public ResponseEntity<ApiResponse<List<MaterialIssueDetailResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(materialIssueDetailService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get material issue detail by id")
    public ResponseEntity<ApiResponse<MaterialIssueDetailResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(materialIssueDetailService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update material issue detail")
    public ResponseEntity<ApiResponse<MaterialIssueDetailResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMaterialIssueDetailRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MaterialIssueDetailResponse response = materialIssueDetailService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Material issue detail updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete material issue detail")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        materialIssueDetailService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Material issue detail deleted successfully", null));
    }
}
