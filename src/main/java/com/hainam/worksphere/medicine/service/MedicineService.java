package com.hainam.worksphere.medicine.service;

import com.hainam.worksphere.medicine.domain.Medicine;
import com.hainam.worksphere.medicine.dto.request.CreateMedicineRequest;
import com.hainam.worksphere.medicine.dto.request.UpdateMedicineRequest;
import com.hainam.worksphere.medicine.dto.response.MedicineResponse;
import com.hainam.worksphere.medicine.mapper.MedicineMapper;
import com.hainam.worksphere.medicine.repository.MedicineRepository;
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
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineMapper medicineMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "MEDICINE")
    public MedicineResponse create(CreateMedicineRequest request, UUID createdBy) {
        if (medicineRepository.existsActiveByMedicineCode(request.getMedicineCode())) {
            throw new BusinessRuleViolationException("Medicine code already exists: " + request.getMedicineCode());
        }

        Medicine entity = Medicine.builder()
                .medicineCode(request.getMedicineCode())
                .name(request.getName())
                .medicineType(request.getMedicineType())
                .unit(request.getUnit())
                .manufacturer(request.getManufacturer())
                .description(request.getDescription())
                .createdBy(createdBy)
                .build();

        Medicine saved = medicineRepository.save(entity);
        AuditContext.registerCreated(saved);
        return medicineMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MedicineResponse> getAll() {
        return medicineRepository.findAllActive().stream().map(medicineMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public MedicineResponse getById(UUID id) {
        Medicine entity = medicineRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", id.toString()));
        return medicineMapper.toResponse(entity);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "MEDICINE")
    public MedicineResponse update(UUID id, UpdateMedicineRequest request, UUID updatedBy) {
        Medicine entity = medicineRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", id.toString()));

        AuditContext.snapshot(entity);

        if (request.getName() != null) entity.setName(request.getName());
        if (request.getMedicineType() != null) entity.setMedicineType(request.getMedicineType());
        if (request.getUnit() != null) entity.setUnit(request.getUnit());
        if (request.getManufacturer() != null) entity.setManufacturer(request.getManufacturer());
        if (request.getDescription() != null) entity.setDescription(request.getDescription());
        entity.setUpdatedBy(updatedBy);

        Medicine saved = medicineRepository.save(entity);
        AuditContext.registerUpdated(saved);
        return medicineMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "MEDICINE")
    public void delete(UUID id, UUID deletedBy) {
        Medicine entity = medicineRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicine", id.toString()));

        AuditContext.registerDeleted(entity);

        entity.setIsDeleted(true);
        entity.setDeletedAt(Instant.now());
        entity.setDeletedBy(deletedBy);
        medicineRepository.save(entity);
    }
}
