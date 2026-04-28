package com.hainam.worksphere.pigletherd.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.pigletherd.dto.request.CreatePigletHerdGrowthRequest;
import com.hainam.worksphere.pigletherd.dto.request.UpdatePigletHerdGrowthRequest;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdGrowthResponse;
import com.hainam.worksphere.pigletherd.service.PigletHerdGrowthService;
import com.hainam.worksphere.shared.constant.PermissionType;
import com.hainam.worksphere.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/piglet-herd-growth")
@RequiredArgsConstructor
@Tag(name = "Piglet Herd Growth Management")
@SecurityRequirement(name = "Bearer Authentication")
public class PigletHerdGrowthController {

    private final PigletHerdGrowthService pigletHerdGrowthService;

    @PostMapping
    @Operation(summary = "Create piglet herd growth (batch)")
    @RequirePermission(PermissionType.UPDATE_PIGLET_HERD)
    public ResponseEntity<ApiResponse<List<PigletHerdGrowthResponse>>> create(
            @Valid @NotEmpty @RequestBody List<@Valid CreatePigletHerdGrowthRequest> requests,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        List<PigletHerdGrowthResponse> response = pigletHerdGrowthService.createBatch(requests, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Piglet herd growth created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all piglet herd growth records")
    @RequirePermission(PermissionType.VIEW_PIGLET_HERD)
    public ResponseEntity<ApiResponse<List<PigletHerdGrowthResponse>>> getAll(
            @RequestParam(required = false) UUID herdId
    ) {
        if (herdId != null) {
            return ResponseEntity.ok(ApiResponse.success(pigletHerdGrowthService.getByHerdId(herdId)));
        }
        return ResponseEntity.ok(ApiResponse.success(pigletHerdGrowthService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get piglet herd growth by id")
    @RequirePermission(PermissionType.VIEW_PIGLET_HERD)
    public ResponseEntity<ApiResponse<PigletHerdGrowthResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(pigletHerdGrowthService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update piglet herd growth")
    @RequirePermission(PermissionType.UPDATE_PIGLET_HERD)
    public ResponseEntity<ApiResponse<PigletHerdGrowthResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePigletHerdGrowthRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigletHerdGrowthResponse response = pigletHerdGrowthService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Piglet herd growth updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete piglet herd growth")
    @RequirePermission(PermissionType.DELETE_PIGLET_HERD)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        pigletHerdGrowthService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Piglet herd growth deleted successfully", null));
    }
}
