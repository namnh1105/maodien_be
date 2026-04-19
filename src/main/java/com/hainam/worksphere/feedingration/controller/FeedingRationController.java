package com.hainam.worksphere.feedingration.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.feedingration.dto.request.CreateFeedingRationRequest;
import com.hainam.worksphere.feedingration.dto.request.UpdateFeedingRationRequest;
import com.hainam.worksphere.feedingration.dto.response.FeedingRationResponse;
import com.hainam.worksphere.feedingration.service.FeedingRationService;
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
@RequestMapping("/api/v1/feeding-rations")
@RequiredArgsConstructor
@Tag(name = "FeedingRation Management")
@SecurityRequirement(name = "Bearer Authentication")
public class FeedingRationController {

    private final FeedingRationService feedingRationService;

    @PostMapping
    @Operation(summary = "Create feeding ration")
    public ResponseEntity<ApiResponse<FeedingRationResponse>> create(
            @Valid @RequestBody CreateFeedingRationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        FeedingRationResponse response = feedingRationService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feeding ration created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all feeding rations")
    public ResponseEntity<ApiResponse<List<FeedingRationResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(feedingRationService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feeding ration by id")
    public ResponseEntity<ApiResponse<FeedingRationResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(feedingRationService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update feeding ration")
    public ResponseEntity<ApiResponse<FeedingRationResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateFeedingRationRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        FeedingRationResponse response = feedingRationService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Feeding ration updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feeding ration")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        feedingRationService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Feeding ration deleted successfully", null));
    }
}
