package com.hainam.worksphere.materialissue.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.materialissue.dto.request.CreateMaterialIssueRequest;
import com.hainam.worksphere.materialissue.dto.request.UpdateMaterialIssueRequest;
import com.hainam.worksphere.materialissue.dto.response.MaterialIssueResponse;
import com.hainam.worksphere.materialissue.service.MaterialIssueService;
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
@RequestMapping("/api/v1/material-issues")
@RequiredArgsConstructor
@Tag(name = "MaterialIssue Management")
@SecurityRequirement(name = "Bearer Authentication")
public class MaterialIssueController {

    private final MaterialIssueService materialIssueService;

    @PostMapping
    @Operation(summary = "Create material issue")
    public ResponseEntity<ApiResponse<MaterialIssueResponse>> create(
            @Valid @RequestBody CreateMaterialIssueRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MaterialIssueResponse response = materialIssueService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Material issue created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all material issues")
    public ResponseEntity<ApiResponse<List<MaterialIssueResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(materialIssueService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get material issue by id")
    public ResponseEntity<ApiResponse<MaterialIssueResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(materialIssueService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update material issue")
    public ResponseEntity<ApiResponse<MaterialIssueResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMaterialIssueRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MaterialIssueResponse response = materialIssueService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Material issue updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete material issue")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        materialIssueService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Material issue deleted successfully", null));
    }
}
