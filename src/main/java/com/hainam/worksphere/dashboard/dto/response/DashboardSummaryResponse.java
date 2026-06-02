package com.hainam.worksphere.dashboard.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private long totalPigs;
    private long totalSows;
    private long totalBoars;
    private long totalPiglets;
    private long unweanedPiglets;
    private long weanedPiglets;
    private long pregnantPigs;
    private Double totalFeedStock;
    private long totalEmployees;
}
