package com.hainam.worksphere.disease.service;

import com.hainam.worksphere.disease.domain.Disease;
import com.hainam.worksphere.disease.dto.request.CreateDiseaseRequest;
import com.hainam.worksphere.disease.dto.request.UpdateDiseaseRequest;
import com.hainam.worksphere.disease.dto.response.DiseaseResponse;
import com.hainam.worksphere.disease.mapper.DiseaseMapper;
import com.hainam.worksphere.disease.repository.DiseaseRepository;
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
public class DiseaseService {

    private final DiseaseRepository diseaseRepository;
    private final DiseaseMapper diseaseMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "DISEASE")
    public DiseaseResponse create(CreateDiseaseRequest request, UUID createdBy) {
        Disease disease = diseaseMapper.toEntity(request);
        disease.setCreatedBy(createdBy);

        Disease saved = diseaseRepository.save(disease);
        AuditContext.registerCreated(saved);
        return diseaseMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<DiseaseResponse> getAll() {
        return diseaseRepository.findAllActive().stream().map(diseaseMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public DiseaseResponse getById(UUID id) {
        Disease disease = diseaseRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disease", id));
        return diseaseMapper.toResponse(disease);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "DISEASE")
    public DiseaseResponse update(UUID id, UpdateDiseaseRequest request, UUID updatedBy) {
        Disease disease = diseaseRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disease", id));

        AuditContext.snapshot(disease);
        diseaseMapper.updateEntityFromRequest(request, disease);
        disease.setUpdatedBy(updatedBy);

        Disease saved = diseaseRepository.save(disease);
        AuditContext.registerUpdated(saved);
        return diseaseMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "DISEASE")
    public void delete(UUID id, UUID deletedBy) {
        Disease disease = diseaseRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Disease", id));

        AuditContext.registerDeleted(disease);
        disease.setIsDeleted(true);
        disease.setDeletedAt(Instant.now());
        disease.setDeletedBy(deletedBy);
        diseaseRepository.save(disease);
    }
}
