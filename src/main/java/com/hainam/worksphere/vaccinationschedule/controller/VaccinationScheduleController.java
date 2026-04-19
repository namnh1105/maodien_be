package com.hainam.worksphere.vaccinationschedule.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.shared.dto.ApiResponse;
import com.hainam.worksphere.vaccinationschedule.dto.request.CreateVaccinationScheduleRequest;
import com.hainam.worksphere.vaccinationschedule.dto.request.UpdateVaccinationScheduleRequest;
import com.hainam.worksphere.vaccinationschedule.dto.response.VaccinationScheduleResponse;
import com.hainam.worksphere.vaccinationschedule.service.VaccinationScheduleService;
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
@RequestMapping("/api/v1/vaccination-schedules")
@RequiredArgsConstructor
@Tag(name = "VaccinationSchedule Management")
@SecurityRequirement(name = "Bearer Authentication")
public class VaccinationScheduleController {

    private final VaccinationScheduleService vaccinationScheduleService;

    @PostMapping
    @Operation(summary = "Create vaccination schedule")
    public ResponseEntity<ApiResponse<VaccinationScheduleResponse>> create(
            @Valid @RequestBody CreateVaccinationScheduleRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        VaccinationScheduleResponse response = vaccinationScheduleService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Vaccination schedule created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all vaccination schedules")
    public ResponseEntity<ApiResponse<List<VaccinationScheduleResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(vaccinationScheduleService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vaccination schedule by id")
    public ResponseEntity<ApiResponse<VaccinationScheduleResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(vaccinationScheduleService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update vaccination schedule")
    public ResponseEntity<ApiResponse<VaccinationScheduleResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateVaccinationScheduleRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        VaccinationScheduleResponse response = vaccinationScheduleService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Vaccination schedule updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vaccination schedule")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        vaccinationScheduleService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Vaccination schedule deleted successfully", null));
    }
}
