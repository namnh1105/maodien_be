package com.hainam.worksphere.medicine.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.medicine.dto.request.CreateMedicineRequest;
import com.hainam.worksphere.medicine.dto.request.UpdateMedicineRequest;
import com.hainam.worksphere.medicine.dto.response.MedicineResponse;
import com.hainam.worksphere.medicine.service.MedicineService;
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
@RequestMapping("/api/v1/medicines")
@RequiredArgsConstructor
@Tag(name = "Medicine Management")
@SecurityRequirement(name = "Bearer Authentication")
public class MedicineController {

    private final MedicineService medicineService;

    @PostMapping
    @Operation(summary = "Create medicine")
    public ResponseEntity<ApiResponse<MedicineResponse>> create(
            @Valid @RequestBody CreateMedicineRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MedicineResponse response = medicineService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Medicine created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all medicines")
    public ResponseEntity<ApiResponse<List<MedicineResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(medicineService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get medicine by id")
    public ResponseEntity<ApiResponse<MedicineResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(medicineService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update medicine")
    public ResponseEntity<ApiResponse<MedicineResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMedicineRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MedicineResponse response = medicineService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Medicine updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete medicine")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        medicineService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Medicine deleted successfully", null));
    }
}
