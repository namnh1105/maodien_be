package com.hainam.worksphere.feedingration.service;

import com.hainam.worksphere.feedingration.domain.FeedingRation;
import com.hainam.worksphere.feedingration.dto.request.CreatePenFeedingRequest;
import com.hainam.worksphere.feedingration.dto.request.CreateFeedingRationRequest;
import com.hainam.worksphere.feedingration.dto.request.CreatePigletHerdFeedingRequest;
import com.hainam.worksphere.feedingration.dto.request.UpdateFeedingRationRequest;
import com.hainam.worksphere.feedingration.dto.response.FeedingRecordResponse;
import com.hainam.worksphere.feedingration.dto.response.FeedingRationResponse;
import com.hainam.worksphere.feedingration.mapper.FeedingRationMapper;
import com.hainam.worksphere.feedingration.repository.FeedingRationRepository;
import com.hainam.worksphere.feedingrationdetail.domain.FeedingRationDetail;
import com.hainam.worksphere.feedingrationdetail.repository.FeedingRationDetailRepository;
import com.hainam.worksphere.livestockmaterial.domain.LivestockMaterial;
import com.hainam.worksphere.livestockmaterial.repository.LivestockMaterialRepository;
import com.hainam.worksphere.pen.repository.PenRepository;
import com.hainam.worksphere.pigletherd.domain.PigletHerd;
import com.hainam.worksphere.pigletherd.repository.PigletHerdRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedingRationService {

    private final FeedingRationRepository feedingRationRepository;
    private final FeedingRationMapper feedingRationMapper;
    private final FeedingRationDetailRepository feedingRationDetailRepository;
    private final LivestockMaterialRepository livestockMaterialRepository;
    private final PenRepository penRepository;
    private final PigletHerdRepository pigletHerdRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "FEEDING_RATION")
    public FeedingRationResponse create(CreateFeedingRationRequest request, UUID createdBy) {
        FeedingRation feedingRation = feedingRationMapper.toEntity(request);
        feedingRation.setCreatedBy(createdBy);

        FeedingRation saved = feedingRationRepository.save(feedingRation);
        AuditContext.registerCreated(saved);
        return feedingRationMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "FEEDING_RATION", actionCode = "FEED_PEN")
    public FeedingRecordResponse feedPen(CreatePenFeedingRequest request, UUID createdBy) {
        penRepository.findActiveById(request.getPenId())
                .orElseThrow(() -> new ResourceNotFoundException("Pen", request.getPenId()));
        LivestockMaterial feed = livestockMaterialRepository.findActiveById(request.getFeedId())
                .orElseThrow(() -> new ResourceNotFoundException("LivestockMaterial", request.getFeedId()));

        int feedingNumber = (int) feedingRationRepository.countActiveByPenIdAndRationDate(
                request.getPenId(),
                request.getFeedingDate()
        ) + 1;

        FeedingRation ration = FeedingRation.builder()
                .penId(request.getPenId())
                .rationDate(request.getFeedingDate())
                .averageIntake(request.getFeedAmount())
                .note(request.getNote())
                .createdBy(createdBy)
                .build();
        FeedingRation savedRation = feedingRationRepository.save(ration);

        FeedingRationDetail detail = FeedingRationDetail.builder()
                .rationId(savedRation.getId())
                .feed(feed)
                .totalFeedAmount(request.getFeedAmount())
                .createdBy(createdBy)
                .build();
        FeedingRationDetail savedDetail = feedingRationDetailRepository.save(detail);

        AuditContext.registerCreated(savedRation);
        AuditContext.registerCreated(savedDetail);
        return buildFeedingRecordResponse(savedRation, savedDetail, null, feedingNumber);
    }

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "FEEDING_RATION", actionCode = "FEED_PIGLET_HERD")
    public FeedingRecordResponse feedPigletHerd(CreatePigletHerdFeedingRequest request, UUID createdBy) {
        PigletHerd herd = pigletHerdRepository.findActiveById(request.getHerdId())
                .orElseThrow(() -> new ResourceNotFoundException("PigletHerd", request.getHerdId()));
        if (herd.getPenId() == null) {
            throw new ResourceNotFoundException("PigletHerd pen", request.getHerdId());
        }

        CreatePenFeedingRequest penRequest = CreatePenFeedingRequest.builder()
                .penId(herd.getPenId())
                .feedId(request.getFeedId())
                .feedAmount(request.getFeedAmount())
                .feedingDate(request.getFeedingDate())
                .note(request.getNote())
                .build();
        FeedingRecordResponse response = feedPen(penRequest, createdBy);
        response.setHerdId(request.getHerdId());
        return response;
    }

    @Transactional(readOnly = true)
    public List<FeedingRationResponse> getAll() {
        return feedingRationRepository.findAllActive().stream().map(feedingRationMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public FeedingRationResponse getById(UUID id) {
        FeedingRation feedingRation = feedingRationRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedingRation", id));
        return feedingRationMapper.toResponse(feedingRation);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "FEEDING_RATION")
    public FeedingRationResponse update(UUID id, UpdateFeedingRationRequest request, UUID updatedBy) {
        FeedingRation feedingRation = feedingRationRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedingRation", id));

        AuditContext.snapshot(feedingRation);
        feedingRationMapper.updateEntityFromRequest(request, feedingRation);
        feedingRation.setUpdatedBy(updatedBy);

        FeedingRation saved = feedingRationRepository.save(feedingRation);
        AuditContext.registerUpdated(saved);
        return feedingRationMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "FEEDING_RATION")
    public void delete(UUID id, UUID deletedBy) {
        FeedingRation feedingRation = feedingRationRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedingRation", id));

        AuditContext.registerDeleted(feedingRation);
        feedingRation.setIsDeleted(true);
        feedingRation.setDeletedAt(Instant.now());
        feedingRation.setDeletedBy(deletedBy);
        feedingRationRepository.save(feedingRation);
    }

    private FeedingRecordResponse buildFeedingRecordResponse(
            FeedingRation ration,
            FeedingRationDetail detail,
            UUID herdId,
            int feedingNumber
    ) {
        LivestockMaterial feed = detail.getFeed();
        return FeedingRecordResponse.builder()
                .rationId(ration.getId())
                .detailId(detail.getId())
                .penId(ration.getPenId())
                .herdId(herdId)
                .feedId(feed == null ? null : feed.getId())
                .feedName(feed == null ? null : feed.getName())
                .feedingDate(ration.getRationDate())
                .feedingNumber(feedingNumber)
                .feedAmount(detail.getTotalFeedAmount())
                .note(ration.getNote())
                .build();
    }
}
