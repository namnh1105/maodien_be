package com.hainam.worksphere.pigletherd.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.pigletherd.dto.request.CreatePigletHerdRequest;
import com.hainam.worksphere.pigletherd.dto.request.MergePigletHerdRequest;
import com.hainam.worksphere.pigletherd.dto.request.SplitPigletHerdRequest;
import com.hainam.worksphere.pigletherd.dto.request.UpdatePigletHerdRequest;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdDetailResponse;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdResponse;
import com.hainam.worksphere.pigletherd.service.PigletHerdService;
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
@RequestMapping("/api/v1/piglet-herds")
@RequiredArgsConstructor
@Tag(name = "Piglet Herd Management")
@SecurityRequirement(name = "Bearer Authentication")
public class PigletHerdController {

    private final PigletHerdService pigletHerdService;

    @PostMapping
    @Operation(summary = "Create piglet herd")
    @RequirePermission(PermissionType.CREATE_PIGLET_HERD)
    public ResponseEntity<ApiResponse<PigletHerdResponse>> create(
            @Valid @RequestBody CreatePigletHerdRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigletHerdResponse response = pigletHerdService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Piglet herd created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all piglet herds")
    @RequirePermission(PermissionType.VIEW_PIGLET_HERD)
    public ResponseEntity<ApiResponse<List<PigletHerdResponse>>> getAll(
            @RequestParam(required = false) UUID motherId
    ) {
        if (motherId != null) {
            return ResponseEntity.ok(ApiResponse.success(pigletHerdService.getByMotherId(motherId)));
        }
        return ResponseEntity.ok(ApiResponse.success(pigletHerdService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get piglet herd by id")
    @RequirePermission(PermissionType.VIEW_PIGLET_HERD)
    public ResponseEntity<ApiResponse<PigletHerdResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(pigletHerdService.getById(id)));
    }

    @GetMapping("/{id}/detail")
    @Operation(summary = "Get piglet herd detail")
    @RequirePermission(PermissionType.VIEW_PIGLET_HERD)
    public ResponseEntity<ApiResponse<PigletHerdDetailResponse>> getDetailById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(pigletHerdService.getDetailById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update piglet herd")
    @RequirePermission(PermissionType.UPDATE_PIGLET_HERD)
    public ResponseEntity<ApiResponse<PigletHerdResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePigletHerdRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigletHerdResponse response = pigletHerdService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Piglet herd updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete piglet herd")
    @RequirePermission(PermissionType.DELETE_PIGLET_HERD)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        pigletHerdService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Piglet herd deleted successfully", null));
    }

    @PostMapping("/split")
    @Operation(summary = "Split piglet herd")
    @RequirePermission(PermissionType.UPDATE_PIGLET_HERD)
    public ResponseEntity<ApiResponse<PigletHerdResponse>> split(
            @Valid @RequestBody SplitPigletHerdRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigletHerdResponse response = pigletHerdService.splitHerd(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Piglet herd split successfully", response));
    }

    @PostMapping("/merge")
    @Operation(summary = "Merge piglet herds")
    @RequirePermission(PermissionType.UPDATE_PIGLET_HERD)
    public ResponseEntity<ApiResponse<PigletHerdResponse>> merge(
            @Valid @RequestBody MergePigletHerdRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigletHerdResponse response = pigletHerdService.mergeHerd(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Piglet herds merged successfully", response));
    }
}
