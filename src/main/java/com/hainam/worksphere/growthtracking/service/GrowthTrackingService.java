package com.hainam.worksphere.growthtracking.service;

import com.hainam.worksphere.growthtracking.domain.GrowthTracking;
import com.hainam.worksphere.growthtracking.dto.request.CreateGrowthTrackingRequest;
import com.hainam.worksphere.growthtracking.dto.request.UpdateGrowthTrackingRequest;
import com.hainam.worksphere.growthtracking.dto.response.GrowthTrackingResponse;
import com.hainam.worksphere.growthtracking.mapper.GrowthTrackingMapper;
import com.hainam.worksphere.growthtracking.repository.GrowthTrackingRepository;
import com.hainam.worksphere.feedingrationdetail.repository.FeedingRationDetailRepository;
import com.hainam.worksphere.penpig.domain.PenPig;
import com.hainam.worksphere.penpig.repository.PenPigRepository;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GrowthTrackingService {

    private final GrowthTrackingRepository growthTrackingRepository;
    private final GrowthTrackingMapper growthTrackingMapper;
    private final PigRepository pigRepository;
    private final PenPigRepository penPigRepository;
    private final FeedingRationDetailRepository feedingRationDetailRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "GROWTH_TRACKING")
    public List<GrowthTrackingResponse> createBatch(List<CreateGrowthTrackingRequest> requests, UUID createdBy) {
        List<GrowthTracking> entities = requests.stream().map(request -> {
            ensurePigExists(request.getPigId());
            GrowthTracking tracking = GrowthTracking.builder()
                    .pigId(request.getPigId())
                    .trackingDate(request.getTrackingDate())
                    .litterLength(request.getLitterLength())
                    .chestGirth(request.getChestGirth())
                    .weight(request.getWeight())
                    .note(request.getNote())
                    .createdBy(createdBy)
                    .build();
            applyCalculatedMetrics(tracking);
            return tracking;
        }).toList();

        List<GrowthTracking> saved = growthTrackingRepository.saveAll(entities);
        saved.forEach(AuditContext::registerCreated);
        return saved.stream().map(this::toResponseWithEarTag).toList();
    }

    @Transactional(readOnly = true)
    public List<GrowthTrackingResponse> getAll() {
        return growthTrackingRepository.findAllActive().stream().map(this::toResponseWithEarTag).toList();
    }

    @Transactional(readOnly = true)
    public GrowthTrackingResponse getById(UUID id) {
        GrowthTracking tracking = growthTrackingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrowthTracking", id.toString()));
        return toResponseWithEarTag(tracking);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "GROWTH_TRACKING")
    public GrowthTrackingResponse update(UUID id, UpdateGrowthTrackingRequest request, UUID updatedBy) {
        GrowthTracking tracking = growthTrackingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrowthTracking", id.toString()));

        AuditContext.snapshot(tracking);

        if (request.getPigId() != null) tracking.setPigId(request.getPigId());
        if (request.getTrackingDate() != null) tracking.setTrackingDate(request.getTrackingDate());
        if (request.getLitterLength() != null) tracking.setLitterLength(request.getLitterLength());
        if (request.getChestGirth() != null) tracking.setChestGirth(request.getChestGirth());
        if (request.getWeight() != null) tracking.setWeight(request.getWeight());
        if (request.getNote() != null) tracking.setNote(request.getNote());
        ensurePigExists(tracking.getPigId());
        applyCalculatedMetrics(tracking);
        tracking.setUpdatedBy(updatedBy);

        GrowthTracking saved = growthTrackingRepository.save(tracking);
        AuditContext.registerUpdated(saved);
        return toResponseWithEarTag(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "GROWTH_TRACKING")
    public void delete(UUID id, UUID deletedBy) {
        GrowthTracking tracking = growthTrackingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GrowthTracking", id.toString()));

        AuditContext.registerDeleted(tracking);

        tracking.setIsDeleted(true);
        tracking.setDeletedAt(Instant.now());
        tracking.setDeletedBy(deletedBy);
        growthTrackingRepository.save(tracking);
    }

    private GrowthTrackingResponse toResponseWithEarTag(GrowthTracking growthTracking) {
        GrowthTrackingResponse response = growthTrackingMapper.toResponse(growthTracking);
        if (growthTracking.getPigId() != null) {
            response.setPigEarTag(pigRepository.findActiveById(growthTracking.getPigId()).map(p -> p.getEarTag()).orElse(null));
        }
        return response;
    }

    private void ensurePigExists(UUID pigId) {
        pigRepository.findActiveById(pigId)
                .orElseThrow(() -> new ResourceNotFoundException("Pig", pigId.toString()));
    }

    private void applyCalculatedMetrics(GrowthTracking tracking) {
        tracking.setGrowthRate(null);
        tracking.setAdg(null);
        tracking.setFcr(null);

        if (tracking.getPigId() == null || tracking.getTrackingDate() == null || tracking.getWeight() == null) {
            return;
        }

        GrowthTracking previous = growthTrackingRepository
                .findPreviousActiveByPigId(tracking.getPigId(), tracking.getTrackingDate())
                .stream()
                .findFirst()
                .orElse(null);
        if (previous == null || previous.getWeight() == null || previous.getTrackingDate() == null) {
            return;
        }

        double growthRate = tracking.getWeight() - previous.getWeight();
        tracking.setGrowthRate(growthRate);

        long days = ChronoUnit.DAYS.between(previous.getTrackingDate(), tracking.getTrackingDate());
        if (days > 0) {
            tracking.setAdg(growthRate / days);
        }

        if (growthRate > 0) {
            Double feedAmount = calculateAverageFeedForPig(
                    tracking.getPigId(),
                    previous.getTrackingDate(),
                    tracking.getTrackingDate()
            );
            if (feedAmount != null) {
                tracking.setFcr(feedAmount / growthRate);
            }
        }
    }

    private Double calculateAverageFeedForPig(UUID pigId, LocalDate startDate, LocalDate endDate) {
        List<PenPig> assignments = penPigRepository.findCurrentByPigId(pigId);
        if (assignments.isEmpty() || assignments.get(0).getPenId() == null) {
            return null;
        }

        UUID penId = assignments.get(0).getPenId();
        long pigCount = penPigRepository.countCurrentPigsByPenId(penId);
        if (pigCount <= 0) {
            return null;
        }

        Double totalFeed = feedingRationDetailRepository.sumTotalFeedByPenAndDateRange(penId, startDate, endDate);
        if (totalFeed == null) {
            return null;
        }
        return totalFeed / pigCount;
    }
}
