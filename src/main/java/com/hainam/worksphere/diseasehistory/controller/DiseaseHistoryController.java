package com.hainam.worksphere.diseasehistory.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.diseasehistory.dto.request.*;
import com.hainam.worksphere.diseasehistory.dto.response.DiseaseHistoryResponse;
import com.hainam.worksphere.diseasehistory.service.DiseaseHistoryService;
import com.hainam.worksphere.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/disease-histories")
@RequiredArgsConstructor
@Tag(name = "Disease History Management")
@SecurityRequirement(name = "Bearer Authentication")
public class DiseaseHistoryController {
    private final DiseaseHistoryService service;

    @PostMapping
    @Operation(summary = "Create disease history")
    public ResponseEntity<ApiResponse<DiseaseHistoryResponse>> create(@Valid @RequestBody CreateDiseaseHistoryRequest r, @AuthenticationPrincipal UserPrincipal u) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Disease history created successfully", service.create(r, u.getId())));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<DiseaseHistoryResponse>>> getAll() { return ResponseEntity.ok(ApiResponse.success(service.getAll())); }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DiseaseHistoryResponse>> getById(@PathVariable UUID id) { return ResponseEntity.ok(ApiResponse.success(service.getById(id))); }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DiseaseHistoryResponse>> update(@PathVariable UUID id, @Valid @RequestBody UpdateDiseaseHistoryRequest r, @AuthenticationPrincipal UserPrincipal u) { return ResponseEntity.ok(ApiResponse.success("Disease history updated successfully", service.update(id, r, u.getId()))); }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id, @AuthenticationPrincipal UserPrincipal u) { service.delete(id, u.getId()); return ResponseEntity.ok(ApiResponse.success("Disease history deleted successfully", null)); }
}
