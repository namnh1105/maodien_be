package com.hainam.worksphere.feed.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.feed.dto.request.CreateFeedRequest;
import com.hainam.worksphere.feed.dto.request.UpdateFeedRequest;
import com.hainam.worksphere.feed.dto.response.FeedResponse;
import com.hainam.worksphere.feed.service.FeedService;
import com.hainam.worksphere.shared.constant.PermissionType;
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
@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
@Tag(name = "Feed Management")
@SecurityRequirement(name = "Bearer Authentication")
public class FeedController {

    private final FeedService feedService;

    @PostMapping
    @Operation(summary = "Create feed")
    @RequirePermission(PermissionType.CREATE_FEED)
    public ResponseEntity<ApiResponse<FeedResponse>> create(
            @Valid @RequestBody CreateFeedRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        FeedResponse response = feedService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Feed created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all feeds")
    @RequirePermission(PermissionType.VIEW_FEED)
    public ResponseEntity<ApiResponse<List<FeedResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(feedService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get feed by id")
    @RequirePermission(PermissionType.VIEW_FEED)
    public ResponseEntity<ApiResponse<FeedResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(feedService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update feed")
    @RequirePermission(PermissionType.UPDATE_FEED)
    public ResponseEntity<ApiResponse<FeedResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateFeedRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        FeedResponse response = feedService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Feed updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete feed")
    @RequirePermission(PermissionType.DELETE_FEED)
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        feedService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Feed deleted successfully", null));
    }
}
