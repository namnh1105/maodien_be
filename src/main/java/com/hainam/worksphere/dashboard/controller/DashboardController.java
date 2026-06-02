package com.hainam.worksphere.dashboard.controller;

import com.hainam.worksphere.dashboard.dto.response.DailyFeedConsumptionResponse;
import com.hainam.worksphere.dashboard.dto.response.DashboardBucketResponse;
import com.hainam.worksphere.dashboard.dto.response.DashboardOverviewResponse;
import com.hainam.worksphere.dashboard.dto.response.DashboardSummaryResponse;
import com.hainam.worksphere.dashboard.dto.response.MatingSuccessRateResponse;
import com.hainam.worksphere.dashboard.dto.response.MonthlyCostResponse;
import com.hainam.worksphere.dashboard.dto.response.MonthlyRevenueResponse;
import com.hainam.worksphere.dashboard.dto.response.SurvivalRateResponse;
import com.hainam.worksphere.dashboard.service.DashboardService;
import com.hainam.worksphere.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Tag(name = "Dashboard")
@SecurityRequirement(name = "Bearer Authentication")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    @Operation(summary = "Get dashboard overview")
    public ResponseEntity<ApiResponse<DashboardOverviewResponse>> getOverview(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getOverview(year, weekStart)));
    }

    @GetMapping("/summary")
    @Operation(summary = "Get dashboard summary")
    public ResponseEntity<ApiResponse<DashboardSummaryResponse>> getSummary() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getSummary()));
    }

    @GetMapping("/weight-distribution")
    @Operation(summary = "Get pig weight distribution")
    public ResponseEntity<ApiResponse<List<DashboardBucketResponse>>> getWeightDistribution() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getWeightDistribution()));
    }

    @GetMapping("/feed-consumption")
    @Operation(summary = "Get feed consumption by day")
    public ResponseEntity<ApiResponse<List<DailyFeedConsumptionResponse>>> getFeedConsumption(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart
    ) {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getFeedConsumption(weekStart)));
    }

    @GetMapping("/survival-rate")
    @Operation(summary = "Get piglet survival rate")
    public ResponseEntity<ApiResponse<SurvivalRateResponse>> getSurvivalRate() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getSurvivalRate()));
    }

    @GetMapping("/mating-success-rate")
    @Operation(summary = "Get mating success rate")
    public ResponseEntity<ApiResponse<MatingSuccessRateResponse>> getMatingSuccessRate() {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getMatingSuccessRate()));
    }

    @GetMapping("/monthly-revenue")
    @Operation(summary = "Get monthly revenue")
    public ResponseEntity<ApiResponse<List<MonthlyRevenueResponse>>> getMonthlyRevenue(
            @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getMonthlyRevenue(year)));
    }

    @GetMapping("/monthly-import-cost")
    @Operation(summary = "Get monthly import cost")
    public ResponseEntity<ApiResponse<List<MonthlyCostResponse>>> getMonthlyImportCost(
            @RequestParam(required = false) Integer year
    ) {
        return ResponseEntity.ok(ApiResponse.success(dashboardService.getMonthlyImportCost(year)));
    }
}
