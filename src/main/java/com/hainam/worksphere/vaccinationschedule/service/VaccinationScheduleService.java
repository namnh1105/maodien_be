package com.hainam.worksphere.vaccinationschedule.service;

import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import com.hainam.worksphere.vaccinationschedule.domain.VaccinationSchedule;
import com.hainam.worksphere.vaccinationschedule.dto.request.CreateVaccinationScheduleRequest;
import com.hainam.worksphere.vaccinationschedule.dto.request.UpdateVaccinationScheduleRequest;
import com.hainam.worksphere.vaccinationschedule.dto.response.VaccinationScheduleResponse;
import com.hainam.worksphere.vaccinationschedule.mapper.VaccinationScheduleMapper;
import com.hainam.worksphere.vaccinationschedule.repository.VaccinationScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VaccinationScheduleService {

    private final VaccinationScheduleRepository vaccinationScheduleRepository;
    private final VaccinationScheduleMapper vaccinationScheduleMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "VACCINATION_SCHEDULE")
    public VaccinationScheduleResponse create(CreateVaccinationScheduleRequest request, UUID createdBy) {
        VaccinationSchedule vaccinationSchedule = vaccinationScheduleMapper.toEntity(request);
        vaccinationSchedule.setCreatedBy(createdBy);

        VaccinationSchedule saved = vaccinationScheduleRepository.save(vaccinationSchedule);
        AuditContext.registerCreated(saved);
        return vaccinationScheduleMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<VaccinationScheduleResponse> getAll() {
        return vaccinationScheduleRepository.findAllActive().stream().map(vaccinationScheduleMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public VaccinationScheduleResponse getById(UUID id) {
        VaccinationSchedule vaccinationSchedule = vaccinationScheduleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VaccinationSchedule", id));
        return vaccinationScheduleMapper.toResponse(vaccinationSchedule);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "VACCINATION_SCHEDULE")
    public VaccinationScheduleResponse update(UUID id, UpdateVaccinationScheduleRequest request, UUID updatedBy) {
        VaccinationSchedule vaccinationSchedule = vaccinationScheduleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VaccinationSchedule", id));

        AuditContext.snapshot(vaccinationSchedule);
        vaccinationScheduleMapper.updateEntityFromRequest(request, vaccinationSchedule);
        vaccinationSchedule.setUpdatedBy(updatedBy);

        VaccinationSchedule saved = vaccinationScheduleRepository.save(vaccinationSchedule);
        AuditContext.registerUpdated(saved);
        return vaccinationScheduleMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "VACCINATION_SCHEDULE")
    public void delete(UUID id, UUID deletedBy) {
        VaccinationSchedule vaccinationSchedule = vaccinationScheduleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VaccinationSchedule", id));

        AuditContext.registerDeleted(vaccinationSchedule);
        vaccinationSchedule.setIsDeleted(true);
        vaccinationSchedule.setDeletedAt(Instant.now());
        vaccinationSchedule.setDeletedBy(deletedBy);
        vaccinationScheduleRepository.save(vaccinationSchedule);
    }
}
