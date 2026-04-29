package com.hainam.worksphere.feed.service;

import com.hainam.worksphere.feed.domain.Feed;
import com.hainam.worksphere.feed.dto.request.CreateFeedRequest;
import com.hainam.worksphere.feed.dto.request.UpdateFeedRequest;
import com.hainam.worksphere.feed.dto.response.FeedResponse;
import com.hainam.worksphere.feed.mapper.FeedMapper;
import com.hainam.worksphere.feed.repository.FeedRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.FeedNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Deprecated
@RequiredArgsConstructor
public class FeedService {

    private final FeedRepository feedRepository;
    private final FeedMapper feedMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "FEED")
    public FeedResponse create(CreateFeedRequest request, UUID createdBy) {
        if (feedRepository.existsActiveByName(request.getName())) {
            throw new BusinessRuleViolationException("Feed name already exists: " + request.getName());
        }

        Feed feed = Feed.builder()
                .name(request.getName())
                .unit(request.getUnit())
                .createdBy(createdBy)
                .build();

        Feed saved = feedRepository.save(feed);
        AuditContext.registerCreated(saved);
        return feedMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<FeedResponse> getAll() {
        return feedRepository.findAllActive().stream().map(feedMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public FeedResponse getById(UUID id) {
        Feed feed = feedRepository.findActiveById(id)
                .orElseThrow(() -> FeedNotFoundException.byId(id.toString()));
        return feedMapper.toResponse(feed);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "FEED")
    public FeedResponse update(UUID id, UpdateFeedRequest request, UUID updatedBy) {
        Feed feed = feedRepository.findActiveById(id)
                .orElseThrow(() -> FeedNotFoundException.byId(id.toString()));

        AuditContext.snapshot(feed);

        if (request.getName() != null) feed.setName(request.getName());
        if (request.getUnit() != null) feed.setUnit(request.getUnit());
        feed.setUpdatedBy(updatedBy);

        Feed saved = feedRepository.save(feed);
        AuditContext.registerUpdated(saved);
        return feedMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "FEED")
    public void delete(UUID id, UUID deletedBy) {
        Feed feed = feedRepository.findActiveById(id)
                .orElseThrow(() -> FeedNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(feed);

        feed.setIsDeleted(true);
        feed.setDeletedAt(Instant.now());
        feed.setDeletedBy(deletedBy);
        feedRepository.save(feed);
    }
}
