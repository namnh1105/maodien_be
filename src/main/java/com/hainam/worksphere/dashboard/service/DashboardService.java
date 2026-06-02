package com.hainam.worksphere.dashboard.service;

import com.hainam.worksphere.dashboard.dto.response.DailyFeedConsumptionResponse;
import com.hainam.worksphere.dashboard.dto.response.DashboardBucketResponse;
import com.hainam.worksphere.dashboard.dto.response.DashboardOverviewResponse;
import com.hainam.worksphere.dashboard.dto.response.DashboardSummaryResponse;
import com.hainam.worksphere.dashboard.dto.response.MatingSuccessRateResponse;
import com.hainam.worksphere.dashboard.dto.response.MonthlyCostResponse;
import com.hainam.worksphere.dashboard.dto.response.MonthlyRevenueResponse;
import com.hainam.worksphere.dashboard.dto.response.SurvivalRateResponse;
import com.hainam.worksphere.employee.domain.EmploymentStatus;
import com.hainam.worksphere.employee.repository.EmployeeRepository;
import com.hainam.worksphere.feedingrationdetail.repository.FeedingRationDetailRepository;
import com.hainam.worksphere.growthtracking.domain.GrowthTracking;
import com.hainam.worksphere.growthtracking.repository.GrowthTrackingRepository;
import com.hainam.worksphere.livestockmaterial.domain.MaterialType;
import com.hainam.worksphere.livestockmaterial.repository.LivestockMaterialRepository;
import com.hainam.worksphere.materialreceipt.repository.MaterialReceiptRepository;
import com.hainam.worksphere.mating.repository.MatingRepository;
import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.domain.PigType;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.pigletherd.domain.PigletHerd;
import com.hainam.worksphere.pigletherd.domain.PigletHerdSale;
import com.hainam.worksphere.pigletherd.domain.PigletHerdStatus;
import com.hainam.worksphere.pigletherd.repository.PigletHerdRepository;
import com.hainam.worksphere.pigletherd.repository.PigletHerdSaleRepository;
import com.hainam.worksphere.reproductioncycle.domain.ReproductionCycle;
import com.hainam.worksphere.reproductioncycle.repository.ReproductionCycleRepository;
import com.hainam.worksphere.sale.domain.Sale;
import com.hainam.worksphere.sale.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final List<String> WEIGHT_BUCKETS = List.of("0-20", "20-40", "40-60", "60-80", "80-100", "100-120", "120+");

    private final PigRepository pigRepository;
    private final PigletHerdRepository pigletHerdRepository;
    private final GrowthTrackingRepository growthTrackingRepository;
    private final ReproductionCycleRepository reproductionCycleRepository;
    private final MatingRepository matingRepository;
    private final LivestockMaterialRepository livestockMaterialRepository;
    private final EmployeeRepository employeeRepository;
    private final FeedingRationDetailRepository feedingRationDetailRepository;
    private final SaleRepository saleRepository;
    private final PigletHerdSaleRepository pigletHerdSaleRepository;
    private final MaterialReceiptRepository materialReceiptRepository;

    @Transactional(readOnly = true)
    public DashboardOverviewResponse getOverview(Integer year, LocalDate weekStart) {
        int effectiveYear = year == null ? LocalDate.now().getYear() : year;
        LocalDate effectiveWeekStart = normalizeWeekStart(weekStart);

        return DashboardOverviewResponse.builder()
                .summary(getSummary())
                .weightDistribution(getWeightDistribution())
                .feedConsumption(getFeedConsumption(effectiveWeekStart))
                .survivalRate(getSurvivalRate())
                .matingSuccessRate(getMatingSuccessRate())
                .monthlyRevenue(getMonthlyRevenue(effectiveYear))
                .monthlyImportCost(getMonthlyImportCost(effectiveYear))
                .build();
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {
        List<Pig> pigs = pigRepository.findAllActive();
        List<PigletHerd> herds = pigletHerdRepository.findAllActive();

        long pigletCount = herds.stream()
                .filter(herd -> !Boolean.TRUE.equals(herd.getIsSold()))
                .map(PigletHerd::getQuantity)
                .filter(quantity -> quantity != null)
                .mapToLong(Integer::longValue)
                .sum();

        return DashboardSummaryResponse.builder()
                .totalPigs(pigs.size())
                .totalSows(pigs.stream().filter(p -> p.getType() == PigType.NAI || p.getType() == PigType.NAI_THIT).count())
                .totalBoars(pigs.stream().filter(p -> p.getType() == PigType.NOC || p.getType() == PigType.NOC_THIT).count())
                .totalPiglets(pigletCount)
                .unweanedPiglets(sumPigletQuantityByStatus(herds, PigletHerdStatus.UNWEANED))
                .weanedPiglets(sumPigletQuantityByStatus(herds, PigletHerdStatus.WEANED))
                .pregnantPigs(reproductionCycleRepository.findAllActivePregnant().size())
                .totalFeedStock(totalFeedStock())
                .totalEmployees(employeeRepository.findActiveByEmploymentStatus(EmploymentStatus.ACTIVE).size())
                .build();
    }

    @Transactional(readOnly = true)
    public List<DashboardBucketResponse> getWeightDistribution() {
        List<Pig> pigs = pigRepository.findAllActive();
        List<UUID> pigIds = pigs.stream().map(Pig::getId).toList();
        Map<UUID, GrowthTracking> latestGrowthByPigId = pigIds.isEmpty()
                ? Map.of()
                : growthTrackingRepository.findActiveByPigIds(pigIds).stream()
                        .collect(Collectors.toMap(
                                GrowthTracking::getPigId,
                                Function.identity(),
                                (first, ignored) -> first
                        ));

        Map<String, Long> counts = WEIGHT_BUCKETS.stream().collect(Collectors.toMap(Function.identity(), ignored -> 0L));
        for (Pig pig : pigs) {
            Double weight = latestGrowthByPigId.containsKey(pig.getId())
                    ? latestGrowthByPigId.get(pig.getId()).getWeight()
                    : pig.getBirthWeight();
            String bucket = toWeightBucket(weight);
            counts.put(bucket, counts.get(bucket) + 1);
        }

        return WEIGHT_BUCKETS.stream()
                .map(label -> DashboardBucketResponse.builder().label(label).count(counts.get(label)).build())
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DailyFeedConsumptionResponse> getFeedConsumption(LocalDate weekStart) {
        LocalDate startDate = normalizeWeekStart(weekStart);
        LocalDate endDate = startDate.plusDays(6);
        Map<LocalDate, Double> feedByDate = new HashMap<>();
        feedingRationDetailRepository.sumTotalFeedByDateRange(startDate, endDate)
                .forEach(row -> feedByDate.put((LocalDate) row[0], (Double) row[1]));

        List<DailyFeedConsumptionResponse> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = startDate.plusDays(i);
            result.add(DailyFeedConsumptionResponse.builder()
                    .date(date)
                    .totalFeedAmount(feedByDate.getOrDefault(date, 0D))
                    .build());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public SurvivalRateResponse getSurvivalRate() {
        List<ReproductionCycle> cycles = reproductionCycleRepository.findAllActive();
        long born = sum(cycles, ReproductionCycle::getBornCount);
        long alive = sum(cycles, ReproductionCycle::getAliveCount);
        long crushed = sum(cycles, ReproductionCycle::getCrushedCount);
        long stillborn = sum(cycles, ReproductionCycle::getDeadCount);
        long deformed = sum(cycles, ReproductionCycle::getDeformedCount);

        return SurvivalRateResponse.builder()
                .bornCount(born)
                .aliveCount(alive)
                .crushedCount(crushed)
                .stillbornCount(stillborn)
                .deformedCount(deformed)
                .aliveRate(percent(alive, born))
                .crushedRate(percent(crushed, born))
                .stillbornRate(percent(stillborn, born))
                .deformedRate(percent(deformed, born))
                .build();
    }

    @Transactional(readOnly = true)
    public MatingSuccessRateResponse getMatingSuccessRate() {
        long totalMatings = matingRepository.findAllActive().size();
        long successfulMatings = reproductionCycleRepository.findAllActive().stream()
                .map(ReproductionCycle::getMatingId)
                .filter(id -> id != null)
                .distinct()
                .count();
        return MatingSuccessRateResponse.builder()
                .totalMatings(totalMatings)
                .successfulMatings(successfulMatings)
                .successRate(percent(successfulMatings, totalMatings))
                .build();
    }

    @Transactional(readOnly = true)
    public List<MonthlyRevenueResponse> getMonthlyRevenue(Integer year) {
        int effectiveYear = year == null ? LocalDate.now().getYear() : year;
        List<Sale> meatPigSales = saleRepository.findActiveBySaleDateBetween(
                LocalDate.of(effectiveYear, 1, 1),
                LocalDate.of(effectiveYear, 12, 31)
        );
        List<PigletHerdSale> pigletHerdSales = pigletHerdSaleRepository.findActiveBySaleDateBetween(
                LocalDate.of(effectiveYear, 1, 1),
                LocalDate.of(effectiveYear, 12, 31)
        );

        List<MonthlyRevenueResponse> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            final int currentMonth = month;
            double pigletRevenue = pigletHerdSales.stream()
                    .filter(sale -> sale.getSaleDate().getMonthValue() == currentMonth)
                    .map(PigletHerdSale::getPrice)
                    .filter(price -> price != null)
                    .mapToDouble(Double::doubleValue)
                    .sum();
            double meatRevenue = meatPigSales.stream()
                    .filter(sale -> sale.getSaleDate().getMonthValue() == currentMonth)
                    .map(Sale::getPrice)
                    .filter(price -> price != null)
                    .mapToDouble(Double::doubleValue)
                    .sum();
            result.add(MonthlyRevenueResponse.builder()
                    .month(month)
                    .pigletHerdRevenue(pigletRevenue)
                    .meatPigRevenue(meatRevenue)
                    .totalRevenue(pigletRevenue + meatRevenue)
                    .build());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<MonthlyCostResponse> getMonthlyImportCost(Integer year) {
        int effectiveYear = year == null ? LocalDate.now().getYear() : year;
        List<MonthlyCostResponse> result = new ArrayList<>();
        var receipts = materialReceiptRepository.findAllActive().stream()
                .filter(receipt -> receipt.getReceiptDate() != null && receipt.getReceiptDate().getYear() == effectiveYear)
                .toList();

        for (int month = 1; month <= 12; month++) {
            final int currentMonth = month;
            double totalCost = receipts.stream()
                    .filter(receipt -> receipt.getReceiptDate().getMonthValue() == currentMonth)
                    .map(receipt -> receipt.getTotalAmount() == null ? 0D : receipt.getTotalAmount())
                    .mapToDouble(Double::doubleValue)
                    .sum();
            result.add(MonthlyCostResponse.builder().month(month).totalCost(totalCost).build());
        }
        return result;
    }

    private long sumPigletQuantityByStatus(List<PigletHerd> herds, PigletHerdStatus status) {
        return herds.stream()
                .filter(herd -> herd.getStatus() == status)
                .filter(herd -> !Boolean.TRUE.equals(herd.getIsSold()))
                .map(PigletHerd::getQuantity)
                .filter(quantity -> quantity != null)
                .mapToLong(Integer::longValue)
                .sum();
    }

    private Double totalFeedStock() {
        return livestockMaterialRepository.findAllActive().stream()
                .filter(material -> material.getMaterialType() == MaterialType.FEED)
                .map(material -> material.getQuantity() == null ? 0D : material.getQuantity())
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    private String toWeightBucket(Double weight) {
        if (weight == null || weight < 20) return "0-20";
        if (weight < 40) return "20-40";
        if (weight < 60) return "40-60";
        if (weight < 80) return "60-80";
        if (weight < 100) return "80-100";
        if (weight < 120) return "100-120";
        return "120+";
    }

    private LocalDate normalizeWeekStart(LocalDate weekStart) {
        return (weekStart == null ? LocalDate.now() : weekStart)
                .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
    }

    private long sum(List<ReproductionCycle> cycles, Function<ReproductionCycle, Integer> getter) {
        return cycles.stream()
                .map(getter)
                .filter(value -> value != null)
                .mapToLong(Integer::longValue)
                .sum();
    }

    private Double percent(long numerator, long denominator) {
        if (denominator <= 0) return 0D;
        return numerator * 100D / denominator;
    }
}
