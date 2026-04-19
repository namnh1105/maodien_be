package com.hainam.worksphere.area.service;

import com.hainam.worksphere.area.domain.Area;
import com.hainam.worksphere.area.dto.request.CreateAreaRequest;
import com.hainam.worksphere.area.dto.request.UpdateAreaRequest;
import com.hainam.worksphere.area.dto.response.AreaResponse;
import com.hainam.worksphere.area.mapper.AreaMapper;
import com.hainam.worksphere.area.repository.AreaRepository;
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
public class AreaService {

    private final AreaRepository areaRepository;
    private final AreaMapper areaMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "AREA")
    public AreaResponse create(CreateAreaRequest request, UUID createdBy) {
        Area area = areaMapper.toEntity(request);
        area.setCreatedBy(createdBy);

        Area saved = areaRepository.save(area);
        AuditContext.registerCreated(saved);
        return areaMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<AreaResponse> getAll() {
        return areaRepository.findAllActive().stream().map(areaMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AreaResponse getById(UUID id) {
        Area area = areaRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));
        return areaMapper.toResponse(area);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "AREA")
    public AreaResponse update(UUID id, UpdateAreaRequest request, UUID updatedBy) {
        Area area = areaRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));

        AuditContext.snapshot(area);
        areaMapper.updateEntityFromRequest(request, area);
        area.setUpdatedBy(updatedBy);

        Area saved = areaRepository.save(area);
        AuditContext.registerUpdated(saved);
        return areaMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "AREA")
    public void delete(UUID id, UUID deletedBy) {
        Area area = areaRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Area", id));

        AuditContext.registerDeleted(area);
        area.setIsDeleted(true);
        area.setDeletedAt(Instant.now());
        area.setDeletedBy(deletedBy);
        areaRepository.save(area);
    }
}
