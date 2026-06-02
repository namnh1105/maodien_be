package com.hainam.worksphere.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardOverviewResponse {
    private DashboardSummaryResponse summary;
    private List<DashboardBucketResponse> weightDistribution;
    private List<DailyFeedConsumptionResponse> feedConsumption;
    private SurvivalRateResponse survivalRate;
    private MatingSuccessRateResponse matingSuccessRate;
    private List<MonthlyRevenueResponse> monthlyRevenue;
    private List<MonthlyCostResponse> monthlyImportCost;
}
