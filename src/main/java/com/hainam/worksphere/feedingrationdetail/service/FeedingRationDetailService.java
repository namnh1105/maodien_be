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

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "FEEDING_RATION_DETAIL")
    public FeedingRationDetailResponse create(CreateFeedingRationDetailRequest request, UUID createdBy) {
        FeedingRationDetail feedingRationDetail = feedingRationDetailMapper.toEntity(request);
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
