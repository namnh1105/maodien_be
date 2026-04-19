package com.hainam.worksphere.area.controller;

import com.hainam.worksphere.area.dto.request.CreateAreaRequest;
import com.hainam.worksphere.area.dto.request.UpdateAreaRequest;
import com.hainam.worksphere.area.dto.response.AreaResponse;
import com.hainam.worksphere.area.service.AreaService;
import com.hainam.worksphere.auth.security.UserPrincipal;
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
@RequestMapping("/api/v1/areas")
@RequiredArgsConstructor
@Tag(name = "Area Management")
@SecurityRequirement(name = "Bearer Authentication")
public class AreaController {

    private final AreaService areaService;

    @PostMapping
    @Operation(summary = "Create area")
    public ResponseEntity<ApiResponse<AreaResponse>> create(
            @Valid @RequestBody CreateAreaRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        AreaResponse response = areaService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Area created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all areas")
    public ResponseEntity<ApiResponse<List<AreaResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(areaService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get area by id")
    public ResponseEntity<ApiResponse<AreaResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(areaService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update area")
    public ResponseEntity<ApiResponse<AreaResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateAreaRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        AreaResponse response = areaService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Area updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete area")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        areaService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Area deleted successfully", null));
    }
}
