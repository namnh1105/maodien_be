package com.hainam.worksphere.vaccination.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.shared.constant.PermissionType;
import com.hainam.worksphere.shared.dto.ApiResponse;
import com.hainam.worksphere.vaccination.dto.request.CreateVaccinationRequest;
import com.hainam.worksphere.vaccination.dto.request.UpdateVaccinationRequest;
import com.hainam.worksphere.vaccination.dto.response.VaccinationResponse;
import com.hainam.worksphere.vaccination.service.VaccinationService;
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
@RequestMapping("/api/v1/vaccinations")
@RequiredArgsConstructor
@Tag(name = "Vaccination Management")
@SecurityRequirement(name = "Bearer Authentication")
public class VaccinationController {

    private final VaccinationService vaccinationService;

    @PostMapping
    @Operation(summary = "Create vaccination")
    @RequirePermission(PermissionType.CREATE_VACCINATION)
    public ResponseEntity<ApiResponse<VaccinationResponse>> create(
            @Valid @RequestBody CreateVaccinationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        VaccinationResponse response = vaccinationService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Vaccination created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all vaccinations")
    @RequirePermission(PermissionType.VIEW_VACCINATION)
    public ResponseEntity<ApiResponse<List<VaccinationResponse>>> getAll(
            @RequestParam(required = false) UUID pigId,
            @RequestParam(required = false) UUID employeeId
    ) {
        if (pigId != null) {
            return ResponseEntity.ok(ApiResponse.success(vaccinationService.getByPigId(pigId)));
        }
        if (employeeId != null) {
            return ResponseEntity.ok(ApiResponse.success(vaccinationService.getByEmployeeId(employeeId)));
        }
        return ResponseEntity.ok(ApiResponse.success(vaccinationService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vaccination by id")
    @RequirePermission(PermissionType.VIEW_VACCINATION)
    public ResponseEntity<ApiResponse<VaccinationResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(vaccinationService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vaccination")
    @RequirePermission(PermissionType.UPDATE_VACCINATION)
    public ResponseEntity<ApiResponse<VaccinationResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateVaccinationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        VaccinationResponse response = vaccinationService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Vaccination updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vaccination")
    @RequirePermission(PermissionType.DELETE_VACCINATION)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        vaccinationService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Vaccination deleted successfully", null));
    }
}
