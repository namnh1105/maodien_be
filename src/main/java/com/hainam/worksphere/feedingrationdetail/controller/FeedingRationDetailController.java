package com.hainam.worksphere.feedingrationdetail.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.feedingrationdetail.dto.request.CreateFeedingRationDetailRequest;
import com.hainam.worksphere.feedingrationdetail.dto.request.UpdateFeedingRationDetailRequest;
import com.hainam.worksphere.feedingrationdetail.dto.response.FeedingRationDetailResponse;
import com.hainam.worksphere.feedingrationdetail.service.FeedingRationDetailService;
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
@RequestMapping("/api/v1/feeding-ration-details")
@RequiredArgsConstructor
@Tag(name = "FeedingRationDetail Management")
@SecurityRequirement(name = "Bearer Authentication")
public class FeedingRationDetailController {

    private final FeedingRationDetailService feedingRationDetailService;

    @PostMapping
    @Operation(summary = "Create feeding ration detail")
    public ResponseEntity<ApiResponse<FeedingRationDetailResponse>> create(
            @Valid @RequestBody CreateFeedingRationDetailRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        FeedingRationDetailResponse response = feedingRationDetailService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feeding ration detail created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all feeding ration details")
    public ResponseEntity<ApiResponse<List<FeedingRationDetailResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(feedingRationDetailService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feeding ration detail by id")
    public ResponseEntity<ApiResponse<FeedingRationDetailResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(feedingRationDetailService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update feeding ration detail")
    public ResponseEntity<ApiResponse<FeedingRationDetailResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateFeedingRationDetailRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        FeedingRationDetailResponse response = feedingRationDetailService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Feeding ration detail updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feeding ration detail")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        feedingRationDetailService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Feeding ration detail deleted successfully", null));
    }
}
