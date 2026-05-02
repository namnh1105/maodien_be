package com.hainam.worksphere.pig.service;

import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.domain.PigStatus;
import com.hainam.worksphere.pig.domain.PigType;
import com.hainam.worksphere.pig.dto.request.CreatePigRequest;
import com.hainam.worksphere.pig.dto.request.UpdatePigRequest;
import com.hainam.worksphere.pig.dto.response.PigResponse;
import com.hainam.worksphere.pig.mapper.PigMapper;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.PigNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PigService {

    private final PigRepository pigRepository;
    private final PigMapper pigMapper;
    private final com.hainam.worksphere.breed.repository.BreedRepository breedRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIG")
    public PigResponse create(CreatePigRequest request, UUID createdBy) {
        Pig pig = Pig.builder()
                .earTag(request.getEarTag())
                .birthWeight(request.getBirthWeight())
                .birthDate(request.getBirthDate())
                .type(parsePigType(request.getType()))
                .origin(request.getOrigin())
                .species(request.getSpecies())
                .nippleCount(request.getNippleCount())
                .herdEntryDate(request.getHerdEntryDate())
                .status(parsePigStatus(request.getStatus()))
                .createdBy(createdBy)
                .build();

        Pig saved = pigRepository.save(pig);
        AuditContext.registerCreated(saved);
        return toResponseWithBreedName(saved);
    }

    @Transactional(readOnly = true)
    public List<PigResponse> getAll() {
        return pigRepository.findAllActive().stream().map(this::toResponseWithBreedName).toList();
    }

    @Transactional(readOnly = true)
    public PigResponse getById(UUID id) {
        Pig pig = pigRepository.findActiveById(id)
                .orElseThrow(() -> PigNotFoundException.byId(id.toString()));
        return toResponseWithBreedName(pig);
    }

    @Transactional(readOnly = true)
    public List<PigResponse> getByStatus(String status) {
        PigStatus parsedStatus = parsePigStatus(status);
        return pigRepository.findActiveByStatus(parsedStatus).stream().map(this::toResponseWithBreedName).toList();
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PIG")
    public PigResponse update(UUID id, UpdatePigRequest request, UUID updatedBy) {
        Pig pig = pigRepository.findActiveById(id)
                .orElseThrow(() -> PigNotFoundException.byId(id.toString()));

        AuditContext.snapshot(pig);

        if (request.getEarTag() != null) pig.setEarTag(request.getEarTag());
        if (request.getBirthWeight() != null) pig.setBirthWeight(request.getBirthWeight());
        if (request.getBirthDate() != null) pig.setBirthDate(request.getBirthDate());
        if (request.getType() != null) pig.setType(parsePigType(request.getType()));
        if (request.getOrigin() != null) pig.setOrigin(request.getOrigin());
        if (request.getSpecies() != null) pig.setSpecies(request.getSpecies());
        if (request.getNippleCount() != null) pig.setNippleCount(request.getNippleCount());
        if (request.getHerdEntryDate() != null) pig.setHerdEntryDate(request.getHerdEntryDate());
        if (request.getStatus() != null) pig.setStatus(parsePigStatus(request.getStatus()));
        pig.setUpdatedBy(updatedBy);

        Pig saved = pigRepository.save(pig);
        AuditContext.registerUpdated(saved);
        return toResponseWithBreedName(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "PIG")
    public void delete(UUID id, UUID deletedBy) {
        Pig pig = pigRepository.findActiveById(id)
                .orElseThrow(() -> PigNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(pig);

        pig.setIsDeleted(true);
        pig.setDeletedAt(Instant.now());
        pig.setDeletedBy(deletedBy);
        pigRepository.save(pig);
    }

    private PigType parsePigType(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            throw new BusinessRuleViolationException("Pig type is required");
        }
        try {
            return PigType.valueOf(rawType.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleViolationException("Invalid pig type: " + rawType);
        }
    }

    private String generatePigCode(UUID id) {
        String shortId = id.toString().replace("-", "").substring(0, 8).toUpperCase();
        return "PIG-" + shortId;
    }

    private PigStatus parsePigStatus(String rawStatus) {
        if (rawStatus == null || rawStatus.isBlank()) {
            return PigStatus.ACTIVE;
        }
        try {
            return PigStatus.valueOf(rawStatus.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleViolationException("Invalid pig status: " + rawStatus);
        }
    }

    private PigResponse toResponseWithBreedName(Pig pig) {
        PigResponse response = pigMapper.toResponse(pig);
        if (pig.getSpecies() != null) {
            breedRepository.findActiveByCode(pig.getSpecies())
                    .ifPresent(breed -> response.setBreedName(breed.getName()));
        }
        return response;
    }
}
