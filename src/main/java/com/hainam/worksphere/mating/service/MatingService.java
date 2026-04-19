package com.hainam.worksphere.mating.service;

import com.hainam.worksphere.mating.domain.Mating;
import com.hainam.worksphere.mating.dto.request.CreateMatingRequest;
import com.hainam.worksphere.mating.dto.request.UpdateMatingRequest;
import com.hainam.worksphere.mating.dto.response.MatingResponse;
import com.hainam.worksphere.mating.mapper.MatingMapper;
import com.hainam.worksphere.mating.repository.MatingRepository;
import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MatingService {

    private final MatingRepository matingRepository;
    private final PigRepository pigRepository;
    private final MatingMapper matingMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "MATING")
    public MatingResponse create(CreateMatingRequest request, UUID createdBy) {
        if (matingRepository.existsActiveByMatingCode(request.getMatingCode())) {
            throw new BusinessRuleViolationException("Mating code already exists: " + request.getMatingCode());
        }

        Mating mating = Mating.builder()
                .matingCode(request.getMatingCode())
                .sowPigId(request.getSowPigId())
                .boarBreedId(request.getBoarBreedId())
                .litterLength(request.getLitterLength())
                .matingRound(request.getMatingRound())
                .employeeId(request.getEmployeeId())
                .matingDate(request.getMatingDate())
                .status(request.getStatus())
                .createdBy(createdBy)
                .build();

        Mating saved = matingRepository.save(mating);
        AuditContext.registerCreated(saved);
        return matingMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MatingResponse> getAll() {
        return matingRepository.findAllActive().stream().map(matingMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MatingResponse getById(UUID id) {
        Mating mating = matingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mating", id.toString()));
        return matingMapper.toResponse(mating);
    }

        @Transactional(readOnly = true)
        public List<MatingResponse> getByMaLon(String maLon) {
        Pig pig = pigRepository.findActiveByPigCode(maLon)
            .or(() -> pigRepository.findActiveByEarTag(maLon))
            .orElseThrow(() -> new ResourceNotFoundException("Pig", maLon));

        return matingRepository.findActiveBySowPigId(pig.getId()).stream()
            .map(matingMapper::toResponse)
            .toList();
        }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "MATING")
    public MatingResponse update(UUID id, UpdateMatingRequest request, UUID updatedBy) {
        Mating mating = matingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mating", id.toString()));

        AuditContext.snapshot(mating);

        if (request.getSowPigId() != null) mating.setSowPigId(request.getSowPigId());
        if (request.getBoarBreedId() != null) mating.setBoarBreedId(request.getBoarBreedId());
        if (request.getLitterLength() != null) mating.setLitterLength(request.getLitterLength());
        if (request.getMatingRound() != null) mating.setMatingRound(request.getMatingRound());
        if (request.getEmployeeId() != null) mating.setEmployeeId(request.getEmployeeId());
        if (request.getMatingDate() != null) mating.setMatingDate(request.getMatingDate());
        if (request.getStatus() != null) mating.setStatus(request.getStatus());
        mating.setUpdatedBy(updatedBy);

        Mating saved = matingRepository.save(mating);
        AuditContext.registerUpdated(saved);
        return matingMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "MATING")
    public void delete(UUID id, UUID deletedBy) {
        Mating mating = matingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mating", id.toString()));

        AuditContext.registerDeleted(mating);

        mating.setIsDeleted(true);
        mating.setDeletedAt(Instant.now());
        mating.setDeletedBy(deletedBy);
        matingRepository.save(mating);
    }
}
