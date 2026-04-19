package com.hainam.worksphere.cullingproposal.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.cullingproposal.dto.request.CreateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.request.UpdateCullingProposalRequest;
import com.hainam.worksphere.cullingproposal.dto.response.CullingProposalResponse;
import com.hainam.worksphere.cullingproposal.service.CullingProposalService;
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
@RequestMapping("/api/v1/culling-proposals")
@RequiredArgsConstructor
@Tag(name = "CullingProposal Management")
@SecurityRequirement(name = "Bearer Authentication")
public class CullingProposalController {

    private final CullingProposalService cullingProposalService;

    @PostMapping
    @Operation(summary = "Create culling proposal")
    public ResponseEntity<ApiResponse<CullingProposalResponse>> create(
            @Valid @RequestBody CreateCullingProposalRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        CullingProposalResponse response = cullingProposalService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Culling proposal created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all culling proposals")
    public ResponseEntity<ApiResponse<List<CullingProposalResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(cullingProposalService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get culling proposal by id")
    public ResponseEntity<ApiResponse<CullingProposalResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(cullingProposalService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update culling proposal")
    public ResponseEntity<ApiResponse<CullingProposalResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateCullingProposalRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        CullingProposalResponse response = cullingProposalService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Culling proposal updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete culling proposal")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        cullingProposalService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Culling proposal deleted successfully", null));
    }
}
