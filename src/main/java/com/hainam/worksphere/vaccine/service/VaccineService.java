package com.hainam.worksphere.vaccine.service;

import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.VaccineNotFoundException;
import com.hainam.worksphere.vaccine.domain.Vaccine;
import com.hainam.worksphere.vaccine.dto.request.CreateVaccineRequest;
import com.hainam.worksphere.vaccine.dto.request.UpdateVaccineRequest;
import com.hainam.worksphere.vaccine.dto.response.VaccineResponse;
import com.hainam.worksphere.vaccine.mapper.VaccineMapper;
import com.hainam.worksphere.vaccine.repository.VaccineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaccineService {

    private final VaccineRepository vaccineRepository;
    private final VaccineMapper vaccineMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "VACCINE")
    public VaccineResponse create(CreateVaccineRequest request, UUID createdBy) {
        if (vaccineRepository.existsActiveByVaccineCode(null)) {
            throw new BusinessRuleViolationException("Vaccine code already exists: " + null);
        }

        Vaccine vaccine = Vaccine.builder()
                .name(request.getName())
                .unit(request.getUnit())
                .treatmentDisease(request.getTreatmentDisease())
                .createdBy(createdBy)
                .build();

        Vaccine saved = vaccineRepository.save(vaccine);
        AuditContext.registerCreated(saved);
        return vaccineMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<VaccineResponse> getAll() {
        return vaccineRepository.findAllActive().stream().map(vaccineMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public VaccineResponse getById(UUID id) {
        Vaccine vaccine = vaccineRepository.findActiveById(id)
                .orElseThrow(() -> VaccineNotFoundException.byId(id.toString()));
        return vaccineMapper.toResponse(vaccine);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "VACCINE")
    public VaccineResponse update(UUID id, UpdateVaccineRequest request, UUID updatedBy) {
        Vaccine vaccine = vaccineRepository.findActiveById(id)
                .orElseThrow(() -> VaccineNotFoundException.byId(id.toString()));

        AuditContext.snapshot(vaccine);

        if (request.getName() != null) vaccine.setName(request.getName());
        if (request.getUnit() != null) vaccine.setUnit(request.getUnit());
        if (request.getTreatmentDisease() != null) vaccine.setTreatmentDisease(request.getTreatmentDisease());
        vaccine.setUpdatedBy(updatedBy);

        Vaccine saved = vaccineRepository.save(vaccine);
        AuditContext.registerUpdated(saved);
        return vaccineMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "VACCINE")
    public void delete(UUID id, UUID deletedBy) {
        Vaccine vaccine = vaccineRepository.findActiveById(id)
                .orElseThrow(() -> VaccineNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(vaccine);

        vaccine.setIsDeleted(true);
        vaccine.setDeletedAt(Instant.now());
        vaccine.setDeletedBy(deletedBy);
        vaccineRepository.save(vaccine);
    }
}
