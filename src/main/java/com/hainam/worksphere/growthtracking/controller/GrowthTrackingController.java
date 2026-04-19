package com.hainam.worksphere.growthtracking.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.growthtracking.dto.request.CreateGrowthTrackingRequest;
import com.hainam.worksphere.growthtracking.dto.request.UpdateGrowthTrackingRequest;
import com.hainam.worksphere.growthtracking.dto.response.GrowthTrackingResponse;
import com.hainam.worksphere.growthtracking.service.GrowthTrackingService;
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
@RequestMapping("/api/v1/growth-tracking")
@RequiredArgsConstructor
@Tag(name = "Growth Tracking Management")
@SecurityRequirement(name = "Bearer Authentication")
public class GrowthTrackingController {

    private final GrowthTrackingService growthTrackingService;

    @PostMapping
    @Operation(summary = "Create growth tracking")
    public ResponseEntity<ApiResponse<GrowthTrackingResponse>> create(
            @Valid @RequestBody CreateGrowthTrackingRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        GrowthTrackingResponse response = growthTrackingService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Growth tracking created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all growth tracking records")
    public ResponseEntity<ApiResponse<List<GrowthTrackingResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(growthTrackingService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get growth tracking by id")
    public ResponseEntity<ApiResponse<GrowthTrackingResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(growthTrackingService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update growth tracking")
    public ResponseEntity<ApiResponse<GrowthTrackingResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateGrowthTrackingRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        GrowthTrackingResponse response = growthTrackingService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Growth tracking updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete growth tracking")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        growthTrackingService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Growth tracking deleted successfully", null));
    }
}
