package com.hainam.worksphere.diseasehistory.service;

import com.hainam.worksphere.diseasehistory.domain.DiseaseHistory;
import com.hainam.worksphere.diseasehistory.dto.request.*;
import com.hainam.worksphere.diseasehistory.dto.response.DiseaseHistoryResponse;
import com.hainam.worksphere.diseasehistory.mapper.DiseaseHistoryMapper;
import com.hainam.worksphere.diseasehistory.repository.DiseaseHistoryRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DiseaseHistoryService {
    private final DiseaseHistoryRepository repo;
    private final DiseaseHistoryMapper mapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "DISEASE_HISTORY")
    public DiseaseHistoryResponse create(CreateDiseaseHistoryRequest r, UUID createdBy) {
        
        DiseaseHistory e = DiseaseHistory.builder().pigId(r.getPigId()).diseaseName(r.getDiseaseName()).sickDate(r.getSickDate()).recoveryDate(r.getRecoveryDate()).severity(r.getSeverity()).expectedTreatmentDays(r.getExpectedTreatmentDays()).status(r.getStatus()).note(r.getNote()).createdBy(createdBy).build();
        e = repo.save(e); AuditContext.registerCreated(e); return mapper.toResponse(e);
    }

    @Transactional(readOnly = true)
    public List<DiseaseHistoryResponse> getAll() { return repo.findAllActive().stream().map(mapper::toResponse).toList(); }

    @Transactional(readOnly = true)
    public DiseaseHistoryResponse getById(UUID id) { return mapper.toResponse(repo.findActiveById(id).orElseThrow(() -> new ResourceNotFoundException("DiseaseHistory", id.toString()))); }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "DISEASE_HISTORY")
    public DiseaseHistoryResponse update(UUID id, UpdateDiseaseHistoryRequest r, UUID updatedBy) {
        DiseaseHistory e = repo.findActiveById(id).orElseThrow(() -> new ResourceNotFoundException("DiseaseHistory", id.toString()));
        AuditContext.snapshot(e);
        if (r.getPigId() != null) e.setPigId(r.getPigId());
        
        if (r.getDiseaseName() != null) e.setDiseaseName(r.getDiseaseName());
        if (r.getSickDate() != null) e.setSickDate(r.getSickDate());
        if (r.getRecoveryDate() != null) e.setRecoveryDate(r.getRecoveryDate());
        if (r.getSeverity() != null) e.setSeverity(r.getSeverity());
        if (r.getExpectedTreatmentDays() != null) e.setExpectedTreatmentDays(r.getExpectedTreatmentDays());
        if (r.getStatus() != null) e.setStatus(r.getStatus());
        if (r.getNote() != null) e.setNote(r.getNote());
        e.setUpdatedBy(updatedBy);
        e = repo.save(e); AuditContext.registerUpdated(e); return mapper.toResponse(e);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "DISEASE_HISTORY")
    public void delete(UUID id, UUID deletedBy) {
        DiseaseHistory e = repo.findActiveById(id).orElseThrow(() -> new ResourceNotFoundException("DiseaseHistory", id.toString()));
        AuditContext.registerDeleted(e);
        e.setIsDeleted(true); e.setDeletedAt(Instant.now()); e.setDeletedBy(deletedBy); repo.save(e);
    }
}
