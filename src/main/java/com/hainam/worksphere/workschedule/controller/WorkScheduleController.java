package com.hainam.worksphere.workschedule.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.shared.dto.ApiResponse;
import com.hainam.worksphere.workschedule.dto.request.CreateWorkScheduleRequest;
import com.hainam.worksphere.workschedule.dto.request.UpdateWorkScheduleRequest;
import com.hainam.worksphere.workschedule.dto.response.WorkScheduleResponse;
import com.hainam.worksphere.workschedule.service.WorkScheduleService;
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
@RequestMapping("/api/v1/work-schedules")
@RequiredArgsConstructor
@Tag(name = "WorkSchedule Management")
@SecurityRequirement(name = "Bearer Authentication")
public class WorkScheduleController {

    private final WorkScheduleService workScheduleService;

    @PostMapping
    @Operation(summary = "Create work schedule")
    public ResponseEntity<ApiResponse<WorkScheduleResponse>> create(
            @Valid @RequestBody CreateWorkScheduleRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        WorkScheduleResponse response = workScheduleService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Work schedule created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all work schedules")
    public ResponseEntity<ApiResponse<List<WorkScheduleResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(workScheduleService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get work schedule by id")
    public ResponseEntity<ApiResponse<WorkScheduleResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(workScheduleService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update work schedule")
    public ResponseEntity<ApiResponse<WorkScheduleResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateWorkScheduleRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        WorkScheduleResponse response = workScheduleService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Work schedule updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete work schedule")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        workScheduleService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Work schedule deleted successfully", null));
    }
}
