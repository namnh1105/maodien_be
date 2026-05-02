package com.hainam.worksphere.penpig.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.penpig.dto.request.CreatePenPigRequest;
import com.hainam.worksphere.penpig.dto.request.UpdatePenPigRequest;
import com.hainam.worksphere.penpig.dto.response.PenPigResponse;
import com.hainam.worksphere.penpig.service.PenPigService;
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
@RequestMapping("/api/v1/pen-pigs")
@RequiredArgsConstructor
@Tag(name = "Pen Pig Management")
@SecurityRequirement(name = "Bearer Authentication")
public class PenPigController {

    private final PenPigService penPigService;

    @PostMapping
    @Operation(summary = "Create pen pig assignment")
    public ResponseEntity<ApiResponse<PenPigResponse>> create(
            @Valid @RequestBody CreatePenPigRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PenPigResponse response = penPigService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Pen pig assignment created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all pen pig assignments")
    public ResponseEntity<ApiResponse<List<PenPigResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(penPigService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get pen pig assignment by id")
    public ResponseEntity<ApiResponse<PenPigResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(penPigService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update pen pig assignment")
    public ResponseEntity<ApiResponse<PenPigResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdatePenPigRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        PenPigResponse response = penPigService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Pen pig assignment updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete pen pig assignment")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        penPigService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Pen pig assignment deleted successfully", null));
    }

    @PostMapping("/transfer")
    @Operation(summary = "Transfer pig/herd to another pen")
    public ResponseEntity<ApiResponse<Void>> transfer(
            @Valid @RequestBody com.hainam.worksphere.penpig.dto.request.TransferPenPigRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        penPigService.transfer(request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Transfer completed successfully", null));
    }
}
