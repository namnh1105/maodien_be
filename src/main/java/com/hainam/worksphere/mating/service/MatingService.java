package com.hainam.worksphere.mating.service;

import com.hainam.worksphere.mating.domain.Mating;
import com.hainam.worksphere.mating.dto.request.CreateMatingRequest;
import com.hainam.worksphere.mating.dto.request.UpdateMatingRequest;
import com.hainam.worksphere.mating.dto.response.MatingResponse;
import com.hainam.worksphere.mating.mapper.MatingMapper;
import com.hainam.worksphere.mating.repository.MatingRepository;
import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.pigsemen.repository.PigSemenRepository;
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
public class MatingService {

    private final MatingRepository matingRepository;
    private final PigRepository pigRepository;
    private final PigSemenRepository pigSemenRepository;
    private final MatingMapper matingMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "MATING")
    public MatingResponse create(CreateMatingRequest request, UUID createdBy) {
        Mating mating = Mating.builder()
                .sowPigId(request.getSowPigId())
                .semenId(request.getSemenId())
                .litterLength(request.getLitterLength())
                .matingRound(request.getMatingRound())
                .employeeId(request.getEmployeeId())
                .matingDate(request.getMatingDate())
                .status(request.getStatus())
                .createdBy(createdBy)
                .build();

        Mating saved = matingRepository.save(mating);
        AuditContext.registerCreated(saved);
        return toResponseWithEnrichment(saved);
    }

    @Transactional(readOnly = true)
    public List<MatingResponse> getAll() {
        return matingRepository.findAllActive().stream().map(this::toResponseWithEnrichment).toList();
    }

    @Transactional(readOnly = true)
    public MatingResponse getById(UUID id) {
        Mating mating = matingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mating", id.toString()));
        return toResponseWithEnrichment(mating);
    }

    @Transactional(readOnly = true)
    public List<MatingResponse> getByMaLon(String maLon) {
        Pig pig = pigRepository.findActiveByEarTag(maLon)
            .orElseThrow(() -> new ResourceNotFoundException("Pig", maLon));

        return matingRepository.findActiveBySowPigId(pig.getId()).stream()
            .map(this::toResponseWithEnrichment)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<MatingResponse> getByPigId(UUID pigId) {
        pigRepository.findActiveById(pigId)
                .orElseThrow(() -> new ResourceNotFoundException("Pig", pigId.toString()));
        return matingRepository.findActiveBySowPigId(pigId).stream()
                .map(this::toResponseWithEnrichment)
                .toList();
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "MATING")
    public MatingResponse update(UUID id, UpdateMatingRequest request, UUID updatedBy) {
        Mating mating = matingRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mating", id.toString()));

        AuditContext.snapshot(mating);

        if (request.getSowPigId() != null) mating.setSowPigId(request.getSowPigId());
        if (request.getSemenId() != null) mating.setSemenId(request.getSemenId());
        if (request.getLitterLength() != null) mating.setLitterLength(request.getLitterLength());
        if (request.getMatingRound() != null) mating.setMatingRound(request.getMatingRound());
        if (request.getEmployeeId() != null) mating.setEmployeeId(request.getEmployeeId());
        if (request.getMatingDate() != null) mating.setMatingDate(request.getMatingDate());
        if (request.getStatus() != null) mating.setStatus(request.getStatus());
        mating.setUpdatedBy(updatedBy);

        Mating saved = matingRepository.save(mating);
        AuditContext.registerUpdated(saved);
        return toResponseWithEnrichment(saved);
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

    private MatingResponse toResponseWithEnrichment(Mating mating) {
        MatingResponse response = matingMapper.toResponse(mating);
        
        if (mating.getSowPigId() != null) {
            pigRepository.findActiveById(mating.getSowPigId()).ifPresent(pig -> {
                response.setSowPigEarTag(pig.getEarTag());
                response.setSowBreed(pig.getSpecies());
            });
        }
        
        if (mating.getSemenId() != null) {
            pigSemenRepository.findActiveById(mating.getSemenId()).ifPresent(semen -> {
                response.setSemenId(semen.getId());
                response.setSemenCode(semen.getCode());
                response.setBoarBreed(semen.getBoarBreed());
            });
        }
        
        return response;
    }
}
