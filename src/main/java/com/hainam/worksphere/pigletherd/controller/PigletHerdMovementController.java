package com.hainam.worksphere.pigletherd.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.pigletherd.dto.request.CreatePigletHerdMovementRequest;
import com.hainam.worksphere.pigletherd.dto.request.UpdatePigletHerdMovementRequest;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdMovementResponse;
import com.hainam.worksphere.pigletherd.service.PigletHerdMovementService;
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
@RequestMapping("/api/v1/piglet-herd-movements")
@RequiredArgsConstructor
@Tag(name = "Piglet Herd Movement Management")
@SecurityRequirement(name = "Bearer Authentication")
public class PigletHerdMovementController {

    private final PigletHerdMovementService pigletHerdMovementService;

    @PostMapping
    @Operation(summary = "Create piglet herd movement")
    @RequirePermission(PermissionType.UPDATE_PIGLET_HERD)
    public ResponseEntity<ApiResponse<PigletHerdMovementResponse>> create(
            @Valid @RequestBody CreatePigletHerdMovementRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigletHerdMovementResponse response = pigletHerdMovementService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Piglet herd movement created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all piglet herd movements")
    @RequirePermission(PermissionType.VIEW_PIGLET_HERD)
    public ResponseEntity<ApiResponse<List<PigletHerdMovementResponse>>> getAll(
            @RequestParam(required = false) UUID herdId
    ) {
        if (herdId != null) {
            return ResponseEntity.ok(ApiResponse.success(pigletHerdMovementService.getByHerdId(herdId)));
        }
        return ResponseEntity.ok(ApiResponse.success(pigletHerdMovementService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get piglet herd movement by id")
    @RequirePermission(PermissionType.VIEW_PIGLET_HERD)
    public ResponseEntity<ApiResponse<PigletHerdMovementResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(pigletHerdMovementService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update piglet herd movement")
    @RequirePermission(PermissionType.UPDATE_PIGLET_HERD)
    public ResponseEntity<ApiResponse<PigletHerdMovementResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePigletHerdMovementRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigletHerdMovementResponse response = pigletHerdMovementService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Piglet herd movement updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete piglet herd movement")
    @RequirePermission(PermissionType.DELETE_PIGLET_HERD)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        pigletHerdMovementService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Piglet herd movement deleted successfully", null));
    }
}
