package com.hainam.worksphere.breed.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.breed.dto.request.CreateBreedRequest;
import com.hainam.worksphere.breed.dto.request.UpdateBreedRequest;
import com.hainam.worksphere.breed.dto.response.BreedResponse;
import com.hainam.worksphere.breed.service.BreedService;
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
@RequestMapping("/api/v1/breeds")
@RequiredArgsConstructor
@Tag(name = "Breed Management")
@SecurityRequirement(name = "Bearer Authentication")
public class BreedController {

    private final BreedService breedService;

    @PostMapping
    @Operation(summary = "Create breed")
    public ResponseEntity<ApiResponse<BreedResponse>> create(
            @Valid @RequestBody CreateBreedRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        BreedResponse response = breedService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Breed created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all breeds")
    public ResponseEntity<ApiResponse<List<BreedResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(breedService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get breed by id")
    public ResponseEntity<ApiResponse<BreedResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(breedService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update breed")
    public ResponseEntity<ApiResponse<BreedResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBreedRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        BreedResponse response = breedService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Breed updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete breed")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        breedService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Breed deleted successfully", null));
    }
}
