package com.hainam.worksphere.pig.service;

import com.hainam.worksphere.diseasehistory.domain.DiseaseHistory;
import com.hainam.worksphere.diseasehistory.repository.DiseaseHistoryRepository;
import com.hainam.worksphere.growthtracking.domain.GrowthTracking;
import com.hainam.worksphere.growthtracking.repository.GrowthTrackingRepository;
import com.hainam.worksphere.pen.domain.Pen;
import com.hainam.worksphere.pen.repository.PenRepository;
import com.hainam.worksphere.penpig.domain.PenPig;
import com.hainam.worksphere.penpig.repository.PenPigRepository;
import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.dto.response.*;
import com.hainam.worksphere.pig.mapper.PigMapper;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.shared.exception.PigNotFoundException;
import com.hainam.worksphere.vaccination.domain.Vaccination;
import com.hainam.worksphere.vaccination.repository.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PigDetailService {

    private final PigRepository pigRepository;
    private final PigMapper pigMapper;
    private final PenPigRepository penPigRepository;
    private final PenRepository penRepository;
    private final GrowthTrackingRepository growthTrackingRepository;
    private final DiseaseHistoryRepository diseaseHistoryRepository;
    private final VaccinationRepository vaccinationRepository;

    @Transactional(readOnly = true)
    public PigDetailResponse getDetail(UUID pigId) {
        Pig pig = pigRepository.findActiveById(pigId)
                .orElseThrow(() -> PigNotFoundException.byId(pigId.toString()));

        PenPig currentAssignment = penPigRepository.findCurrentByPigId(pigId).stream().findFirst().orElse(null);
        Pen currentPen = currentAssignment == null ? null : penRepository.findActiveById(currentAssignment.getPenId()).orElse(null);

        List<GrowthTracking> growths = growthTrackingRepository.findActiveByPigId(pigId);
        List<DiseaseHistory> diseases = diseaseHistoryRepository.findActiveByPigId(pigId);
        List<Vaccination> vaccinations = vaccinationRepository.findActiveByPigId(pigId);

        List<PigGrowthItemResponse> growthHistory = growths.stream()
                .map(g -> PigGrowthItemResponse.builder()
                        .id(g.getId())
                        .trackingDate(g.getTrackingDate())
                        .litterLength(g.getLitterLength())
                        .chestGirth(g.getChestGirth())
                        .weight(g.getWeight())
                        .adg(g.getAdg())
                        .fcr(g.getFcr())
                        .build())
                .toList();

        List<PigDiseaseHistoryItemResponse> diseaseHistory = diseases.stream()
                .map(d -> PigDiseaseHistoryItemResponse.builder()
                        .id(d.getId())
                        .diseaseName(d.getDiseaseName())
                        .sickDate(d.getSickDate())
                        .recoveryDate(d.getRecoveryDate())
                        .status(d.getStatus())
                        .build())
                .toList();

        List<PigVaccinationItemResponse> vaccinationItems = vaccinations.stream()
                .map(v -> PigVaccinationItemResponse.builder()
                        .id(v.getId())
                        .vaccineName(v.getVaccine() != null ? v.getVaccine().getName() : null)
                        .vaccinationDate(v.getVaccinationDate())
                        .dosage(v.getDosage())
                        .note(v.getNote())
                        .build())
                .toList();

        Double currentWeight = growths.stream()
                .max(Comparator.comparing(GrowthTracking::getTrackingDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(GrowthTracking::getWeight)
                .orElse(pig.getBirthWeight());

        Double latestFcr = growths.stream()
                .map(GrowthTracking::getFcr)
                .filter(v -> v != null)
                .findFirst()
                .orElse(null);

        Double adg = calculateAdg(growths);

        return PigDetailResponse.builder()
                .pig(pigMapper.toResponse(pig))
                .currentPenId(currentPen != null ? currentPen.getId() : null)
                .currentPenName(currentPen != null ? currentPen.getName() : null)
                .penEntryDate(currentAssignment != null ? currentAssignment.getEntryDate() : null)
                .currentWeight(currentWeight)
                .adg(adg)
                .fcr(latestFcr)
                .growthHistory(growthHistory)
                .diseaseHistory(diseaseHistory)
                .vaccinations(vaccinationItems)
                .build();
    }

    private Double calculateAdg(List<GrowthTracking> growths) {
        if (growths.size() < 2) {
            return null;
        }

        List<GrowthTracking> sorted = growths.stream()
                .filter(g -> g.getTrackingDate() != null && g.getWeight() != null)
                .sorted(Comparator.comparing(GrowthTracking::getTrackingDate))
                .toList();

        if (sorted.size() < 2) {
            return null;
        }

        GrowthTracking first = sorted.get(0);
        GrowthTracking last = sorted.get(sorted.size() - 1);
        long days = ChronoUnit.DAYS.between(first.getTrackingDate(), last.getTrackingDate());
        if (days <= 0) {
            return null;
        }

        return (last.getWeight() - first.getWeight()) / days;
    }
}
