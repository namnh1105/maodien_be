package com.hainam.worksphere.feedingrationdetail.service;

import com.hainam.worksphere.feedingrationdetail.domain.FeedingRationDetail;
import com.hainam.worksphere.feedingrationdetail.dto.request.CreateFeedingRationDetailRequest;
import com.hainam.worksphere.feedingrationdetail.dto.request.UpdateFeedingRationDetailRequest;
import com.hainam.worksphere.feedingrationdetail.dto.response.FeedingRationDetailResponse;
import com.hainam.worksphere.feedingrationdetail.mapper.FeedingRationDetailMapper;
import com.hainam.worksphere.feedingrationdetail.repository.FeedingRationDetailRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import com.hainam.worksphere.livestockmaterial.domain.LivestockMaterial;
import com.hainam.worksphere.livestockmaterial.repository.LivestockMaterialRepository;
import com.hainam.worksphere.livestockmaterial.domain.MaterialType;
import com.hainam.worksphere.shared.exception.LivestockMaterialNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedingRationDetailService {

    private final FeedingRationDetailRepository feedingRationDetailRepository;
    private final FeedingRationDetailMapper feedingRationDetailMapper;
    private final LivestockMaterialRepository livestockMaterialRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "FEEDING_RATION_DETAIL")
    public FeedingRationDetailResponse create(CreateFeedingRationDetailRequest request, UUID createdBy) {
        LivestockMaterial feed = livestockMaterialRepository.findActiveById(request.getFeedId())
                .orElseThrow(() -> LivestockMaterialNotFoundException.byId(request.getFeedId().toString()));

        if (feed.getMaterialType() != MaterialType.FEED) {
            throw new IllegalArgumentException("Material is not a feed");
        }

        FeedingRationDetail feedingRationDetail = feedingRationDetailMapper.toEntity(request);
        feedingRationDetail.setFeed(feed);
        feedingRationDetail.setCreatedBy(createdBy);

        FeedingRationDetail saved = feedingRationDetailRepository.save(feedingRationDetail);
        AuditContext.registerCreated(saved);
        return feedingRationDetailMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<FeedingRationDetailResponse> getAll() {
        return feedingRationDetailRepository.findAllActive().stream().map(feedingRationDetailMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public FeedingRationDetailResponse getById(UUID id) {
        FeedingRationDetail feedingRationDetail = feedingRationDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedingRationDetail", id));
        return feedingRationDetailMapper.toResponse(feedingRationDetail);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "FEEDING_RATION_DETAIL")
    public FeedingRationDetailResponse update(UUID id, UpdateFeedingRationDetailRequest request, UUID updatedBy) {
        FeedingRationDetail feedingRationDetail = feedingRationDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedingRationDetail", id));

        AuditContext.snapshot(feedingRationDetail);
        feedingRationDetailMapper.updateEntityFromRequest(request, feedingRationDetail);
        
        if (request.getFeedId() != null) {
            LivestockMaterial feed = livestockMaterialRepository.findActiveById(request.getFeedId())
                    .orElseThrow(() -> LivestockMaterialNotFoundException.byId(request.getFeedId().toString()));
            if (feed.getMaterialType() != MaterialType.FEED) {
                throw new IllegalArgumentException("Material is not a feed");
            }
            feedingRationDetail.setFeed(feed);
        }

        feedingRationDetail.setUpdatedBy(updatedBy);

        FeedingRationDetail saved = feedingRationDetailRepository.save(feedingRationDetail);
        AuditContext.registerUpdated(saved);
        return feedingRationDetailMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "FEEDING_RATION_DETAIL")
    public void delete(UUID id, UUID deletedBy) {
        FeedingRationDetail feedingRationDetail = feedingRationDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("FeedingRationDetail", id));

        AuditContext.registerDeleted(feedingRationDetail);
        feedingRationDetail.setIsDeleted(true);
        feedingRationDetail.setDeletedAt(Instant.now());
        feedingRationDetail.setDeletedBy(deletedBy);
        feedingRationDetailRepository.save(feedingRationDetail);
    }
}
