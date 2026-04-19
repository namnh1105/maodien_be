package com.hainam.worksphere.reproductioncycle.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.reproductioncycle.dto.request.CreateReproductionCycleRequest;
import com.hainam.worksphere.reproductioncycle.dto.request.UpdateReproductionCycleRequest;
import com.hainam.worksphere.reproductioncycle.dto.response.ReproductionCycleResponse;
import com.hainam.worksphere.reproductioncycle.service.ReproductionCycleService;
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
@RequestMapping("/api/v1/reproduction-cycles")
@RequiredArgsConstructor
@Tag(name = "Reproduction Cycle Management")
@SecurityRequirement(name = "Bearer Authentication")
public class ReproductionCycleController {

    private final ReproductionCycleService reproductionCycleService;

    @PostMapping
    @Operation(summary = "Create reproduction cycle")
    public ResponseEntity<ApiResponse<ReproductionCycleResponse>> create(
            @Valid @RequestBody CreateReproductionCycleRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ReproductionCycleResponse response = reproductionCycleService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Reproduction cycle created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all reproduction cycles")
    public ResponseEntity<ApiResponse<List<ReproductionCycleResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(reproductionCycleService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get reproduction cycle by id")
    public ResponseEntity<ApiResponse<ReproductionCycleResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(reproductionCycleService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update reproduction cycle")
    public ResponseEntity<ApiResponse<ReproductionCycleResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateReproductionCycleRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        ReproductionCycleResponse response = reproductionCycleService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Reproduction cycle updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete reproduction cycle")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        reproductionCycleService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Reproduction cycle deleted successfully", null));
    }
}
