package com.hainam.worksphere.pig.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.pig.dto.request.CreatePigRequest;
import com.hainam.worksphere.pig.dto.request.UpdatePigRequest;
import com.hainam.worksphere.pig.dto.response.PigResponse;
import com.hainam.worksphere.pig.service.PigService;
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
@RequestMapping("/api/v1/pigs")
@RequiredArgsConstructor
@Tag(name = "Pig Management")
@SecurityRequirement(name = "Bearer Authentication")
public class PigController {

    private final PigService pigService;

    @PostMapping
    @Operation(summary = "Create pig")
    @RequirePermission(PermissionType.CREATE_PIG)
    public ResponseEntity<ApiResponse<PigResponse>> create(
            @Valid @RequestBody CreatePigRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigResponse response = pigService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pig created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all pigs")
    @RequirePermission(PermissionType.VIEW_PIG)
    public ResponseEntity<ApiResponse<List<PigResponse>>> getAll(
            @RequestParam(required = false) String status
    ) {
        if (status != null && !status.isBlank()) {
            return ResponseEntity.ok(ApiResponse.success(pigService.getByStatus(status)));
        }
        return ResponseEntity.ok(ApiResponse.success(pigService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pig by id")
    @RequirePermission(PermissionType.VIEW_PIG)
    public ResponseEntity<ApiResponse<PigResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(pigService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pig")
    @RequirePermission(PermissionType.UPDATE_PIG)
    public ResponseEntity<ApiResponse<PigResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePigRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigResponse response = pigService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Pig updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pig")
    @RequirePermission(PermissionType.DELETE_PIG)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        pigService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Pig deleted successfully", null));
    }
}
