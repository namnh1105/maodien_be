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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
