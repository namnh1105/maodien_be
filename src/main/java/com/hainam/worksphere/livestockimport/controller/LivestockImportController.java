package com.hainam.worksphere.livestockimport.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.authorization.security.RequirePermission;
import com.hainam.worksphere.livestockimport.dto.request.CreateLivestockImportRequest;
import com.hainam.worksphere.livestockimport.dto.response.LivestockImportResponse;
import com.hainam.worksphere.livestockimport.service.LivestockImportService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/livestock-imports")
@RequiredArgsConstructor
@Tag(name = "Livestock Import Management")
@SecurityRequirement(name = "Bearer Authentication")
public class LivestockImportController {

    private final LivestockImportService livestockImportService;

    @PostMapping
    @Operation(summary = "Create livestock material import")
    @RequirePermission(PermissionType.CREATE_WAREHOUSE_IMPORT)
    public ResponseEntity<ApiResponse<LivestockImportResponse>> create(
            @Valid @RequestBody CreateLivestockImportRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        LivestockImportResponse response = livestockImportService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Livestock import created successfully", response));
    }
}
