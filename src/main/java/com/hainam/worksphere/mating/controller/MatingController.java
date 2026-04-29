package com.hainam.worksphere.mating.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.mating.dto.request.CreateMatingRequest;
import com.hainam.worksphere.mating.dto.request.UpdateMatingRequest;
import com.hainam.worksphere.mating.dto.response.MatingResponse;
import com.hainam.worksphere.mating.service.MatingService;
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
@RequestMapping("/api/v1/mating-records")
@RequiredArgsConstructor
@Tag(name = "Mating Management")
@SecurityRequirement(name = "Bearer Authentication")
public class MatingController {

    private final MatingService matingService;

    @PostMapping
    @Operation(summary = "Create mating record")
    public ResponseEntity<ApiResponse<MatingResponse>> create(
            @Valid @RequestBody CreateMatingRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MatingResponse response = matingService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Mating record created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all mating records")
    public ResponseEntity<ApiResponse<List<MatingResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(matingService.getAll()));
    }

    @GetMapping("/by-ma-lon/{maLon}")
    @Operation(summary = "Get mating records by pig ear tag")
    public ResponseEntity<ApiResponse<List<MatingResponse>>> getByMaLon(@PathVariable String maLon) {
        return ResponseEntity.ok(ApiResponse.success(matingService.getByMaLon(maLon)));
    }

    @GetMapping("/by-pig/{pigId}")
    @Operation(summary = "Get mating history by pig id (Lịch sử phối theo id lợn)")
    public ResponseEntity<ApiResponse<List<MatingResponse>>> getByPigId(@PathVariable UUID pigId) {
        return ResponseEntity.ok(ApiResponse.success(matingService.getByPigId(pigId)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get mating record by id")
    public ResponseEntity<ApiResponse<MatingResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(matingService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update mating record")
    public ResponseEntity<ApiResponse<MatingResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateMatingRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        MatingResponse response = matingService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Mating record updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete mating record")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        matingService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Mating record deleted successfully", null));
    }
}
