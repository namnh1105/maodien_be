package com.hainam.worksphere.pen.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.pen.dto.request.CreatePenRequest;
import com.hainam.worksphere.pen.dto.request.UpdatePenRequest;
import com.hainam.worksphere.pen.dto.response.PenResponse;
import com.hainam.worksphere.pen.service.PenService;
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
@RequestMapping("/api/v1/pens")
@RequiredArgsConstructor
@Tag(name = "Pen Management")
@SecurityRequirement(name = "Bearer Authentication")
public class PenController {

    private final PenService penService;

    @PostMapping
    @Operation(summary = "Create pen")
    @RequirePermission(PermissionType.CREATE_PEN)
    public ResponseEntity<ApiResponse<PenResponse>> create(
            @Valid @RequestBody CreatePenRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PenResponse response = penService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pen created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all pens")
    @RequirePermission(PermissionType.VIEW_PEN)
    public ResponseEntity<ApiResponse<List<PenResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(penService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pen by id")
    @RequirePermission(PermissionType.VIEW_PEN)
    public ResponseEntity<ApiResponse<PenResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(penService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pen")
    @RequirePermission(PermissionType.UPDATE_PEN)
    public ResponseEntity<ApiResponse<PenResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePenRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PenResponse response = penService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Pen updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pen")
    @RequirePermission(PermissionType.DELETE_PEN)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        penService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Pen deleted successfully", null));
    }
}
