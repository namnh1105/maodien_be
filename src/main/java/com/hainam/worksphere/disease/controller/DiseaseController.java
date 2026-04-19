package com.hainam.worksphere.disease.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.disease.dto.request.CreateDiseaseRequest;
import com.hainam.worksphere.disease.dto.request.UpdateDiseaseRequest;
import com.hainam.worksphere.disease.dto.response.DiseaseResponse;
import com.hainam.worksphere.disease.service.DiseaseService;
import com.hainam.worksphere.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/diseases")
@RequiredArgsConstructor
@Tag(name = "Disease Management")
@SecurityRequirement(name = "Bearer Authentication")
public class DiseaseController {

    private final DiseaseService diseaseService;

    @PostMapping
    @Operation(summary = "Create disease")
    public ResponseEntity<ApiResponse<DiseaseResponse>> create(
            @Valid @RequestBody CreateDiseaseRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        DiseaseResponse response = diseaseService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Disease created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all diseases")
    public ResponseEntity<ApiResponse<List<DiseaseResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(diseaseService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get disease by id")
    public ResponseEntity<ApiResponse<DiseaseResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(diseaseService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update disease")
    public ResponseEntity<ApiResponse<DiseaseResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDiseaseRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        DiseaseResponse response = diseaseService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Disease updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete disease")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        diseaseService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Disease deleted successfully", null));
    }
}
