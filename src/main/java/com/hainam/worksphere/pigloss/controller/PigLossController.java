package com.hainam.worksphere.pigloss.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.pigloss.dto.request.CreatePigLossRequest;
import com.hainam.worksphere.pigloss.dto.request.UpdatePigLossRequest;
import com.hainam.worksphere.pigloss.dto.response.PigLossResponse;
import com.hainam.worksphere.pigloss.service.PigLossService;
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
@RequestMapping("/api/v1/pig-losses")
@RequiredArgsConstructor
@Tag(name = "Pig Loss Management")
@SecurityRequirement(name = "Bearer Authentication")
public class PigLossController {

    private final PigLossService pigLossService;

    @PostMapping
    @Operation(summary = "Create pig loss")
    public ResponseEntity<ApiResponse<PigLossResponse>> create(
            @Valid @RequestBody CreatePigLossRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigLossResponse response = pigLossService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pig loss created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all pig losses")
    public ResponseEntity<ApiResponse<List<PigLossResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(pigLossService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pig loss by id")
    public ResponseEntity<ApiResponse<PigLossResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(pigLossService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pig loss")
    public ResponseEntity<ApiResponse<PigLossResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePigLossRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigLossResponse response = pigLossService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Pig loss updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pig loss")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        pigLossService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Pig loss deleted successfully", null));
    }
}
