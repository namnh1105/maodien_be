package com.hainam.worksphere.vaccine.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.shared.constant.PermissionType;
import com.hainam.worksphere.shared.dto.ApiResponse;
import com.hainam.worksphere.vaccine.dto.request.CreateVaccineRequest;
import com.hainam.worksphere.vaccine.dto.request.UpdateVaccineRequest;
import com.hainam.worksphere.vaccine.dto.response.VaccineResponse;
import com.hainam.worksphere.vaccine.service.VaccineService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Deprecated
@RequiredArgsConstructor
public class VaccineController {

    private final VaccineService vaccineService;

    @PostMapping
    @Operation(summary = "Create vaccine")
    @RequirePermission(PermissionType.CREATE_VACCINE)
    public ResponseEntity<ApiResponse<VaccineResponse>> create(
            @Valid @RequestBody CreateVaccineRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        VaccineResponse response = vaccineService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Vaccine created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all vaccines")
    @RequirePermission(PermissionType.VIEW_VACCINE)
    public ResponseEntity<ApiResponse<List<VaccineResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(vaccineService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vaccine by id")
    @RequirePermission(PermissionType.VIEW_VACCINE)
    public ResponseEntity<ApiResponse<VaccineResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(vaccineService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vaccine")
    @RequirePermission(PermissionType.UPDATE_VACCINE)
    public ResponseEntity<ApiResponse<VaccineResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateVaccineRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        VaccineResponse response = vaccineService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Vaccine updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vaccine")
    @RequirePermission(PermissionType.DELETE_VACCINE)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        vaccineService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Vaccine deleted successfully", null));
    }
}
