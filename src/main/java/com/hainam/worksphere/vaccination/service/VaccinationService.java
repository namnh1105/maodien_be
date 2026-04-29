package com.hainam.worksphere.vaccination.service;

import com.hainam.worksphere.employee.domain.Employee;
import com.hainam.worksphere.employee.repository.EmployeeRepository;
import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.EmployeeNotFoundException;
import com.hainam.worksphere.shared.exception.PigNotFoundException;
import com.hainam.worksphere.shared.exception.VaccinationNotFoundException;
import com.hainam.worksphere.livestockmaterial.domain.LivestockMaterial;
import com.hainam.worksphere.livestockmaterial.repository.LivestockMaterialRepository;
import com.hainam.worksphere.shared.exception.LivestockMaterialNotFoundException;
import com.hainam.worksphere.livestockmaterial.domain.MaterialType;
import com.hainam.worksphere.vaccination.domain.Vaccination;
import com.hainam.worksphere.vaccination.dto.request.CreateVaccinationRequest;
import com.hainam.worksphere.vaccination.dto.request.UpdateVaccinationRequest;
import com.hainam.worksphere.vaccination.dto.response.VaccinationResponse;
import com.hainam.worksphere.vaccination.mapper.VaccinationMapper;
import com.hainam.worksphere.vaccination.repository.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final PigRepository pigRepository;
    private final LivestockMaterialRepository livestockMaterialRepository;
    private final EmployeeRepository employeeRepository;
    private final VaccinationMapper vaccinationMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "VACCINATION")
    public VaccinationResponse create(CreateVaccinationRequest request, UUID createdBy) {
        Pig pig = pigRepository.findActiveById(request.getPigId())
                .orElseThrow(() -> PigNotFoundException.byId(request.getPigId().toString()));

        LivestockMaterial vaccine = livestockMaterialRepository.findActiveById(request.getVaccineId())
                .orElseThrow(() -> LivestockMaterialNotFoundException.byId(request.getVaccineId().toString()));

        if (vaccine.getMaterialType() != MaterialType.VACCINE) {
            throw new IllegalArgumentException("Material is not a vaccine");
        }

        Vaccination vaccination = Vaccination.builder()
                .pig(pig)
                .vaccine(vaccine)
                .vaccinationDate(request.getVaccinationDate())
                .dosage(request.getDosage())
                .employee(findEmployeeOrNull(request.getEmployeeId()))
                .note(request.getNote())
                .createdBy(createdBy)
                .build();

        Vaccination saved = vaccinationRepository.save(vaccination);
        AuditContext.registerCreated(saved);
        return vaccinationMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<VaccinationResponse> getAll() {
        return vaccinationRepository.findAllActive().stream().map(vaccinationMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public VaccinationResponse getById(UUID id) {
        Vaccination vaccination = vaccinationRepository.findActiveById(id)
                .orElseThrow(() -> VaccinationNotFoundException.byId(id.toString()));
        return vaccinationMapper.toResponse(vaccination);
    }

    @Transactional(readOnly = true)
    public List<VaccinationResponse> getByPigId(UUID pigId) {
        return vaccinationRepository.findActiveByPigId(pigId).stream().map(vaccinationMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<VaccinationResponse> getByEmployeeId(UUID employeeId) {
        return vaccinationRepository.findActiveByEmployeeId(employeeId).stream().map(vaccinationMapper::toResponse).toList();
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "VACCINATION")
    public VaccinationResponse update(UUID id, UpdateVaccinationRequest request, UUID updatedBy) {
        Vaccination vaccination = vaccinationRepository.findActiveById(id)
                .orElseThrow(() -> VaccinationNotFoundException.byId(id.toString()));

        AuditContext.snapshot(vaccination);

        if (request.getPigId() != null) {
            Pig pig = pigRepository.findActiveById(request.getPigId())
                    .orElseThrow(() -> PigNotFoundException.byId(request.getPigId().toString()));
            vaccination.setPig(pig);
        }

        if (request.getVaccineId() != null) {
            LivestockMaterial vaccine = livestockMaterialRepository.findActiveById(request.getVaccineId())
                    .orElseThrow(() -> LivestockMaterialNotFoundException.byId(request.getVaccineId().toString()));
            if (vaccine.getMaterialType() != MaterialType.VACCINE) {
                throw new IllegalArgumentException("Material is not a vaccine");
            }
            vaccination.setVaccine(vaccine);
        }

        if (request.getVaccinationDate() != null) vaccination.setVaccinationDate(request.getVaccinationDate());
        if (request.getDosage() != null) vaccination.setDosage(request.getDosage());
        if (request.getEmployeeId() != null) vaccination.setEmployee(findEmployeeOrNull(request.getEmployeeId()));
        if (request.getNote() != null) vaccination.setNote(request.getNote());
        vaccination.setUpdatedBy(updatedBy);

        Vaccination saved = vaccinationRepository.save(vaccination);
        AuditContext.registerUpdated(saved);
        return vaccinationMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "VACCINATION")
    public void delete(UUID id, UUID deletedBy) {
        Vaccination vaccination = vaccinationRepository.findActiveById(id)
                .orElseThrow(() -> VaccinationNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(vaccination);

        vaccination.setIsDeleted(true);
        vaccination.setDeletedAt(Instant.now());
        vaccination.setDeletedBy(deletedBy);
        vaccinationRepository.save(vaccination);
    }

    private Employee findEmployeeOrNull(UUID employeeId) {
        if (employeeId == null) {
            return null;
        }
        return employeeRepository.findActiveById(employeeId)
                .orElseThrow(() -> EmployeeNotFoundException.byId(employeeId.toString()));
    }
}
