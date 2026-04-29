package com.hainam.worksphere.pig.service;

import com.hainam.worksphere.growthtracking.domain.GrowthTracking;
import com.hainam.worksphere.growthtracking.repository.GrowthTrackingRepository;
import com.hainam.worksphere.mating.domain.Mating;
import com.hainam.worksphere.mating.repository.MatingRepository;
import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.domain.PigType;
import com.hainam.worksphere.pig.dto.response.PregnantPigResponse;
import com.hainam.worksphere.pig.dto.response.PigWithLatestGrowthResponse;
import com.hainam.worksphere.pig.dto.response.SowResponse;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.reproductioncycle.domain.ReproductionCycle;
import com.hainam.worksphere.reproductioncycle.repository.ReproductionCycleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PigQueryService {

    private final PigRepository pigRepository;
    private final GrowthTrackingRepository growthTrackingRepository;
    private final MatingRepository matingRepository;
    private final ReproductionCycleRepository reproductionCycleRepository;

    // ─────────────────────────────────────────────────────────────────
    // API: Danh sách lợn kèm bản ghi tăng trưởng gần nhất
    // ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<PigWithLatestGrowthResponse> getAllWithLatestGrowth() {
        List<Pig> pigs = pigRepository.findAllActive();
        return buildPigWithGrowthList(pigs);
    }

    @Transactional(readOnly = true)
    public List<PigWithLatestGrowthResponse> getAllWithLatestGrowthByType(String type) {
        PigType tempType;
        try {
            tempType = PigType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            tempType = null;
        }
        
        final PigType filterType = tempType;
        List<Pig> pigs = (filterType != null)
                ? pigRepository.findAllActive().stream()
                    .filter(p -> p.getType() == filterType)
                    .toList()
                : pigRepository.findAllActive();
        return buildPigWithGrowthList(pigs);
    }

    private List<PigWithLatestGrowthResponse> buildPigWithGrowthList(List<Pig> pigs) {
        if (pigs.isEmpty()) return List.of();

        List<UUID> pigIds = pigs.stream().map(Pig::getId).toList();

        // Lấy growth records của tất cả pigs trong 1 lần query
        List<GrowthTracking> allGrowths = growthTrackingRepository.findActiveByPigIds(pigIds);

        // Group by pigId, lấy bản ghi mới nhất
        Map<UUID, GrowthTracking> latestGrowthByPigId = allGrowths.stream()
                .collect(Collectors.toMap(
                        GrowthTracking::getPigId,
                        g -> g,
                        (g1, g2) -> {
                            // Ưu tiên bản có trackingDate mới hơn
                            if (g1.getTrackingDate() == null) return g2;
                            if (g2.getTrackingDate() == null) return g1;
                            return g1.getTrackingDate().isAfter(g2.getTrackingDate()) ? g1 : g2;
                        }
                ));

        return pigs.stream().map(pig -> {
            GrowthTracking latest = latestGrowthByPigId.get(pig.getId());
            return PigWithLatestGrowthResponse.builder()
                    .id(pig.getId())
                    .earTag(pig.getEarTag())
                    .type(pig.getType() != null ? pig.getType().name() : null)
                    .species(pig.getSpecies())
                    .status(pig.getStatus() != null ? pig.getStatus().name() : null)
                    .latestTrackingDate(latest != null ? latest.getTrackingDate() : null)
                    .weight(latest != null ? latest.getWeight() : pig.getBirthWeight())
                    .litterLength(latest != null ? latest.getLitterLength() : null)
                    .chestGirth(latest != null ? latest.getChestGirth() : null)
                    .adg(latest != null ? latest.getAdg() : null)
                    .fcr(latest != null ? latest.getFcr() : null)
                    .build();
        }).toList();
    }

    // ─────────────────────────────────────────────────────────────────
    // API: Danh sách lợn nái (SOW)
    // ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<SowResponse> getAllSows() {
        // Lọc lợn nái: type = NAI
        List<Pig> sows = pigRepository.findAllActive().stream()
                .filter(p -> p.getType() == PigType.NAI)
                .toList();

        return buildSowResponseList(sows);
    }

    private List<SowResponse> buildSowResponseList(List<Pig> sows) {
        if (sows.isEmpty()) return List.of();

        return sows.stream().map(sow -> {
            // Lấy lịch sử sinh sản của nái
            List<ReproductionCycle> cycles = reproductionCycleRepository.findActiveBySowPigId(sow.getId());

            // Ngày đẻ gần nhất
            LocalDate lastFarrowDate = cycles.stream()
                    .filter(c -> c.getActualFarrowDate() != null)
                    .max(Comparator.comparing(ReproductionCycle::getActualFarrowDate))
                    .map(ReproductionCycle::getActualFarrowDate)
                    .orElse(null);

            // Tổng số lần mang thai = tổng số records
            int totalPregnancies = cycles.size();

            // Số lần sẩy thai / thất bại (actualFarrowDate IS NULL và status chứa FAILED/ABORTED)
            long miscarriageCount = cycles.stream()
                    .filter(c -> c.getStatus() != null &&
                            (c.getStatus().toUpperCase().contains("FAILED") ||
                             c.getStatus().toUpperCase().contains("ABORTED") ||
                             c.getStatus().toUpperCase().contains("MISCARRIAGE")))
                    .count();

            return SowResponse.builder()
                    .id(sow.getId())
                    .earTag(sow.getEarTag())
                    .type(sow.getType() != null ? sow.getType().name() : null)
                    .species(sow.getSpecies())
                    .lastFarrowDate(lastFarrowDate)
                    .totalPregnancies(totalPregnancies)
                    .miscarriageCount((int) miscarriageCount)
                    .status(sow.getStatus() != null ? sow.getStatus().name() : null)
                    .build();
        }).toList();
    }

    // ─────────────────────────────────────────────────────────────────
    // API: Lịch sử lứa đẻ của lợn nái (Farrowing History)
    // ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<ReproductionCycle> getFarrowingHistoryBySowPigId(UUID sowPigId) {
        return reproductionCycleRepository.findActiveBySowPigId(sowPigId);
    }

    // ─────────────────────────────────────────────────────────────────
    // API: Danh sách lợn đang mang thai
    // ─────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<PregnantPigResponse> getAllPregnant() {
        // Lấy tất cả reproduction cycles chưa đẻ
        List<ReproductionCycle> pregnantCycles = reproductionCycleRepository.findAllActivePregnant();

        return pregnantCycles.stream().map(rc -> {
            // Lấy mating để tìm sowPigId và matingDate
            Mating mating = matingRepository.findActiveById(rc.getMatingId()).orElse(null);
            if (mating == null) return null;

            Pig sow = pigRepository.findActiveById(mating.getSowPigId()).orElse(null);
            if (sow == null) return null;

            // Tính số lần mang thai (pregnancyNumber = tổng số records tính đến bây giờ)
            long pregnancyNumber = reproductionCycleRepository.findActiveBySowPigId(sow.getId()).size();

            return PregnantPigResponse.builder()
                    .id(sow.getId())
                    .earTag(sow.getEarTag())
                    .matingDate(mating.getMatingDate())
                    .conceptionDate(rc.getConceptionDate())
                    .expectedFarrowDate(rc.getExpectedFarrowDate())
                    .pregnancyNumber((int) pregnancyNumber)
                    .status(rc.getStatus())
                    .build();
        }).filter(r -> r != null).toList();
    }
}
