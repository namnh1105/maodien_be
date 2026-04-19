package com.hainam.worksphere.workschedule.service;

import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import com.hainam.worksphere.workschedule.domain.WorkSchedule;
import com.hainam.worksphere.workschedule.dto.request.CreateWorkScheduleRequest;
import com.hainam.worksphere.workschedule.dto.request.UpdateWorkScheduleRequest;
import com.hainam.worksphere.workschedule.dto.response.WorkScheduleResponse;
import com.hainam.worksphere.workschedule.mapper.WorkScheduleMapper;
import com.hainam.worksphere.workschedule.repository.WorkScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;
    private final WorkScheduleMapper workScheduleMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "WORK_SCHEDULE")
    public WorkScheduleResponse create(CreateWorkScheduleRequest request, UUID createdBy) {
        WorkSchedule workSchedule = workScheduleMapper.toEntity(request);
        workSchedule.setCreatedBy(createdBy);

        WorkSchedule saved = workScheduleRepository.save(workSchedule);
        AuditContext.registerCreated(saved);
        return workScheduleMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<WorkScheduleResponse> getAll() {
        return workScheduleRepository.findAllActive().stream().map(workScheduleMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public WorkScheduleResponse getById(UUID id) {
        WorkSchedule workSchedule = workScheduleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkSchedule", id));
        return workScheduleMapper.toResponse(workSchedule);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "WORK_SCHEDULE")
    public WorkScheduleResponse update(UUID id, UpdateWorkScheduleRequest request, UUID updatedBy) {
        WorkSchedule workSchedule = workScheduleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkSchedule", id));

        AuditContext.snapshot(workSchedule);
        workScheduleMapper.updateEntityFromRequest(request, workSchedule);
        workSchedule.setUpdatedBy(updatedBy);

        WorkSchedule saved = workScheduleRepository.save(workSchedule);
        AuditContext.registerUpdated(saved);
        return workScheduleMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "WORK_SCHEDULE")
    public void delete(UUID id, UUID deletedBy) {
        WorkSchedule workSchedule = workScheduleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WorkSchedule", id));

        AuditContext.registerDeleted(workSchedule);
        workSchedule.setIsDeleted(true);
        workSchedule.setDeletedAt(Instant.now());
        workSchedule.setDeletedBy(deletedBy);
        workScheduleRepository.save(workSchedule);
    }
}
