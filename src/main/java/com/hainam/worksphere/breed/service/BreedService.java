package com.hainam.worksphere.breed.service;

import com.hainam.worksphere.breed.domain.Breed;
import com.hainam.worksphere.breed.dto.request.CreateBreedRequest;
import com.hainam.worksphere.breed.dto.request.UpdateBreedRequest;
import com.hainam.worksphere.breed.dto.response.BreedResponse;
import com.hainam.worksphere.breed.mapper.BreedMapper;
import com.hainam.worksphere.breed.repository.BreedRepository;
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
public class BreedService {

    private final BreedRepository breedRepository;
    private final BreedMapper breedMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "BREED")
    public BreedResponse create(CreateBreedRequest request, UUID createdBy) {
        if (breedRepository.existsActiveByName(request.getName())) {
            throw new BusinessRuleViolationException("Breed name already exists: " + request.getName());
        }

        Breed breed = Breed.builder()
                .name(request.getName())
                .code(request.getCode())
                .characteristics(request.getCharacteristics())
                .createdBy(createdBy)
                .build();

        Breed saved = breedRepository.save(breed);
        AuditContext.registerCreated(saved);
        return breedMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BreedResponse> getAll() {
        return breedRepository.findAllActive().stream().map(breedMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public BreedResponse getById(UUID id) {
        Breed breed = breedRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Breed", id.toString()));
        return breedMapper.toResponse(breed);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "BREED")
    public BreedResponse update(UUID id, UpdateBreedRequest request, UUID updatedBy) {
        Breed breed = breedRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Breed", id.toString()));

        AuditContext.snapshot(breed);

        if (request.getName() != null) breed.setName(request.getName());
        if (request.getCode() != null) breed.setCode(request.getCode());
        if (request.getCharacteristics() != null) breed.setCharacteristics(request.getCharacteristics());
        breed.setUpdatedBy(updatedBy);

        Breed saved = breedRepository.save(breed);
        AuditContext.registerUpdated(saved);
        return breedMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "BREED")
    public void delete(UUID id, UUID deletedBy) {
        Breed breed = breedRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Breed", id.toString()));

        AuditContext.registerDeleted(breed);

        breed.setIsDeleted(true);
        breed.setDeletedAt(Instant.now());
        breed.setDeletedBy(deletedBy);
        breedRepository.save(breed);
    }
}

