package com.hainam.worksphere.feedingration.service;

import com.hainam.worksphere.feedingration.domain.FeedingRation;
import com.hainam.worksphere.feedingration.dto.request.CreateFeedingRationRequest;
import com.hainam.worksphere.feedingration.dto.request.UpdateFeedingRationRequest;
import com.hainam.worksphere.feedingration.dto.response.FeedingRationResponse;
import com.hainam.worksphere.feedingration.mapper.FeedingRationMapper;
import com.hainam.worksphere.feedingration.repository.FeedingRationRepository;
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

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "FEEDING_RATION")
    public FeedingRationResponse create(CreateFeedingRationRequest request, UUID createdBy) {
        FeedingRation feedingRation = feedingRationMapper.toEntity(request);
        feedingRation.setCreatedBy(createdBy);

        FeedingRation saved = feedingRationRepository.save(feedingRation);
        AuditContext.registerCreated(saved);
        return feedingRationMapper.toResponse(saved);
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
}
