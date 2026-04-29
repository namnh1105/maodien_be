package com.hainam.worksphere.pigsemen.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.pigsemen.dto.request.CreatePigSemenRequest;
import com.hainam.worksphere.pigsemen.dto.response.PigSemenResponse;
import com.hainam.worksphere.pigsemen.service.PigSemenService;
import com.hainam.worksphere.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.hainam.worksphere.pigsemen.dto.request.UpdatePigSemenRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pig-semen")
@RequiredArgsConstructor
@Tag(name = "Pig Semen Management")
@SecurityRequirement(name = "Bearer Authentication")
public class PigSemenController {

    private final PigSemenService pigSemenService;

    @PostMapping
    @Operation(summary = "Create pig semen")
    public ResponseEntity<ApiResponse<PigSemenResponse>> create(
            @Valid @RequestBody CreatePigSemenRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PigSemenResponse response = pigSemenService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pig semen created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all pig semen")
    public ResponseEntity<ApiResponse<List<PigSemenResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(pigSemenService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pig semen by id")
    public ResponseEntity<ApiResponse<PigSemenResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(pigSemenService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pig semen")
    public ResponseEntity<ApiResponse<PigSemenResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePigSemenRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(ApiResponse.success("Pig semen updated successfully", 
                pigSemenService.update(id, request, userPrincipal.getId())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pig semen")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        pigSemenService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Pig semen deleted successfully", null));
    }
}
