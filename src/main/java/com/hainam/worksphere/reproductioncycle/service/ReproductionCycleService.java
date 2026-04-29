package com.hainam.worksphere.reproductioncycle.service;

import com.hainam.worksphere.reproductioncycle.domain.ReproductionCycle;
import com.hainam.worksphere.reproductioncycle.dto.request.CreateReproductionCycleRequest;
import com.hainam.worksphere.reproductioncycle.dto.request.UpdateReproductionCycleRequest;
import com.hainam.worksphere.reproductioncycle.dto.response.ReproductionCycleResponse;
import com.hainam.worksphere.reproductioncycle.mapper.ReproductionCycleMapper;
import com.hainam.worksphere.reproductioncycle.repository.ReproductionCycleRepository;
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
public class ReproductionCycleService {

    private final ReproductionCycleRepository reproductionCycleRepository;
    private final ReproductionCycleMapper reproductionCycleMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "REPRODUCTION_CYCLE")
    public ReproductionCycleResponse create(CreateReproductionCycleRequest request, UUID createdBy) {
        ReproductionCycle cycle = ReproductionCycle.builder()
                .matingId(request.getMatingId())
                .conceptionDate(request.getConceptionDate())
                .expectedFarrowDate(request.getExpectedFarrowDate())
                .actualFarrowDate(request.getActualFarrowDate())
                .status(request.getStatus())
                .bornCount(request.getBornCount())
                .aliveCount(request.getAliveCount())
                .deadCount(request.getDeadCount())
                .crushedCount(request.getCrushedCount())
                .deformedCount(request.getDeformedCount())
                .averageWeight(request.getAverageWeight())
                .createdBy(createdBy)
                .build();

        ReproductionCycle saved = reproductionCycleRepository.save(cycle);
        AuditContext.registerCreated(saved);
        return reproductionCycleMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<ReproductionCycleResponse> getAll() {
        return reproductionCycleRepository.findAllActive().stream().map(reproductionCycleMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ReproductionCycleResponse getById(UUID id) {
        ReproductionCycle cycle = reproductionCycleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReproductionCycle", id.toString()));
        return reproductionCycleMapper.toResponse(cycle);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "REPRODUCTION_CYCLE")
    public ReproductionCycleResponse update(UUID id, UpdateReproductionCycleRequest request, UUID updatedBy) {
        ReproductionCycle cycle = reproductionCycleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReproductionCycle", id.toString()));

        AuditContext.snapshot(cycle);

        if (request.getMatingId() != null) cycle.setMatingId(request.getMatingId());
        if (request.getConceptionDate() != null) cycle.setConceptionDate(request.getConceptionDate());
        if (request.getExpectedFarrowDate() != null) cycle.setExpectedFarrowDate(request.getExpectedFarrowDate());
        if (request.getActualFarrowDate() != null) cycle.setActualFarrowDate(request.getActualFarrowDate());
        if (request.getStatus() != null) cycle.setStatus(request.getStatus());
        if (request.getBornCount() != null) cycle.setBornCount(request.getBornCount());
        if (request.getAliveCount() != null) cycle.setAliveCount(request.getAliveCount());
        if (request.getDeadCount() != null) cycle.setDeadCount(request.getDeadCount());
        if (request.getCrushedCount() != null) cycle.setCrushedCount(request.getCrushedCount());
        if (request.getDeformedCount() != null) cycle.setDeformedCount(request.getDeformedCount());
        if (request.getAverageWeight() != null) cycle.setAverageWeight(request.getAverageWeight());
        cycle.setUpdatedBy(updatedBy);

        ReproductionCycle saved = reproductionCycleRepository.save(cycle);
        AuditContext.registerUpdated(saved);
        return reproductionCycleMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "REPRODUCTION_CYCLE")
    public void delete(UUID id, UUID deletedBy) {
        ReproductionCycle cycle = reproductionCycleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReproductionCycle", id.toString()));

        AuditContext.registerDeleted(cycle);

        cycle.setIsDeleted(true);
        cycle.setDeletedAt(Instant.now());
        cycle.setDeletedBy(deletedBy);
        reproductionCycleRepository.save(cycle);
    }
}
