package com.hainam.worksphere.pigletherd.service;

import com.hainam.worksphere.pigletherd.domain.PigletHerdGrowth;
import com.hainam.worksphere.feedingrationdetail.repository.FeedingRationDetailRepository;
import com.hainam.worksphere.pigletherd.domain.PigletHerd;
import com.hainam.worksphere.pigletherd.dto.request.CreatePigletHerdGrowthRequest;
import com.hainam.worksphere.pigletherd.dto.request.UpdatePigletHerdGrowthRequest;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdGrowthResponse;
import com.hainam.worksphere.pigletherd.mapper.PigletHerdGrowthMapper;
import com.hainam.worksphere.pigletherd.repository.PigletHerdGrowthRepository;
import com.hainam.worksphere.pigletherd.repository.PigletHerdRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.PigletHerdNotFoundException;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PigletHerdGrowthService {

    private final PigletHerdGrowthRepository pigletHerdGrowthRepository;
    private final PigletHerdRepository pigletHerdRepository;
    private final PigletHerdGrowthMapper pigletHerdGrowthMapper;
    private final FeedingRationDetailRepository feedingRationDetailRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIGLET_HERD_GROWTH")
    public PigletHerdGrowthResponse create(CreatePigletHerdGrowthRequest request, UUID createdBy) {
        PigletHerd herd = ensureHerdExists(request.getHerdId());

        PigletHerdGrowth growth = PigletHerdGrowth.builder()
                .herdId(request.getHerdId())
                .trackingDate(request.getTrackingDate())
                .averageWeight(request.getAverageWeight())
                .averageLitterLength(request.getAverageLitterLength())
                .averageChestGirth(request.getAverageChestGirth())
                .note(request.getNote())
                .createdBy(createdBy)
                .build();
        applyCalculatedMetrics(growth, herd);

        PigletHerdGrowth saved = pigletHerdGrowthRepository.save(growth);
        AuditContext.registerCreated(saved);
        return pigletHerdGrowthMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIGLET_HERD_GROWTH")
    public List<PigletHerdGrowthResponse> createBatch(List<CreatePigletHerdGrowthRequest> requests, UUID createdBy) {
        List<PigletHerdGrowth> entities = requests.stream().map(request -> {
            PigletHerd herd = ensureHerdExists(request.getHerdId());
            PigletHerdGrowth growth = PigletHerdGrowth.builder()
                    .herdId(request.getHerdId())
                    .trackingDate(request.getTrackingDate())
                    .averageWeight(request.getAverageWeight())
                    .averageLitterLength(request.getAverageLitterLength())
                    .averageChestGirth(request.getAverageChestGirth())
                    .note(request.getNote())
                    .createdBy(createdBy)
                    .build();
            applyCalculatedMetrics(growth, herd);
            return growth;
        }).toList();

        List<PigletHerdGrowth> saved = pigletHerdGrowthRepository.saveAll(entities);
        saved.forEach(AuditContext::registerCreated);
        return saved.stream().map(pigletHerdGrowthMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PigletHerdGrowthResponse> getAll() {
        return pigletHerdGrowthRepository.findAllActive().stream().map(pigletHerdGrowthMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<PigletHerdGrowthResponse> getByHerdId(UUID herdId) {
        return pigletHerdGrowthRepository.findActiveByHerdId(herdId).stream().map(pigletHerdGrowthMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PigletHerdGrowthResponse getById(UUID id) {
        PigletHerdGrowth growth = pigletHerdGrowthRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigletHerdGrowth", id.toString()));
        return pigletHerdGrowthMapper.toResponse(growth);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PIGLET_HERD_GROWTH")
    public PigletHerdGrowthResponse update(UUID id, UpdatePigletHerdGrowthRequest request, UUID updatedBy) {
        PigletHerdGrowth growth = pigletHerdGrowthRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigletHerdGrowth", id.toString()));

        AuditContext.snapshot(growth);

        if (request.getTrackingDate() != null) growth.setTrackingDate(request.getTrackingDate());
        if (request.getAverageWeight() != null) growth.setAverageWeight(request.getAverageWeight());
        if (request.getAverageLitterLength() != null) growth.setAverageLitterLength(request.getAverageLitterLength());
        if (request.getAverageChestGirth() != null) growth.setAverageChestGirth(request.getAverageChestGirth());
        if (request.getNote() != null) growth.setNote(request.getNote());
        PigletHerd herd = ensureHerdExists(growth.getHerdId());
        applyCalculatedMetrics(growth, herd);
        growth.setUpdatedBy(updatedBy);

        PigletHerdGrowth saved = pigletHerdGrowthRepository.save(growth);
        AuditContext.registerUpdated(saved);
        return pigletHerdGrowthMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "PIGLET_HERD_GROWTH")
    public void delete(UUID id, UUID deletedBy) {
        PigletHerdGrowth growth = pigletHerdGrowthRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PigletHerdGrowth", id.toString()));

        AuditContext.registerDeleted(growth);

        growth.setIsDeleted(true);
        growth.setDeletedAt(Instant.now());
        growth.setDeletedBy(deletedBy);
        pigletHerdGrowthRepository.save(growth);
    }

    private PigletHerd ensureHerdExists(UUID herdId) {
        return pigletHerdRepository.findActiveById(herdId)
                .orElseThrow(() -> PigletHerdNotFoundException.byId(herdId.toString()));
    }

    private void applyCalculatedMetrics(PigletHerdGrowth growth, PigletHerd herd) {
        growth.setGrowthRate(null);
        growth.setAdg(null);
        growth.setFcr(null);

        if (growth.getHerdId() == null || growth.getTrackingDate() == null || growth.getAverageWeight() == null) {
            return;
        }

        PigletHerdGrowth previous = pigletHerdGrowthRepository
                .findPreviousActiveByHerdId(growth.getHerdId(), growth.getTrackingDate())
                .stream()
                .findFirst()
                .orElse(null);
        if (previous == null || previous.getAverageWeight() == null || previous.getTrackingDate() == null) {
            return;
        }

        double growthRate = growth.getAverageWeight() - previous.getAverageWeight();
        growth.setGrowthRate(growthRate);

        long days = ChronoUnit.DAYS.between(previous.getTrackingDate(), growth.getTrackingDate());
        if (days > 0) {
            growth.setAdg(growthRate / days);
        }

        if (growthRate > 0) {
            Double feedAmount = calculateAverageFeedForHerdPig(herd, previous.getTrackingDate(), growth.getTrackingDate());
            if (feedAmount != null) {
                growth.setFcr(feedAmount / growthRate);
            }
        }
    }

    private Double calculateAverageFeedForHerdPig(PigletHerd herd, LocalDate startDate, LocalDate endDate) {
        if (herd == null || herd.getPenId() == null || herd.getQuantity() == null || herd.getQuantity() <= 0) {
            return null;
        }
        Double totalFeed = feedingRationDetailRepository.sumTotalFeedByPenAndDateRange(herd.getPenId(), startDate, endDate);
        if (totalFeed == null) {
            return null;
        }
        return totalFeed / herd.getQuantity();
    }
}
