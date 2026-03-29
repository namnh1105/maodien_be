package com.hainam.worksphere.pigletherd.service;

import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.pigletherd.domain.PigletHerd;
import com.hainam.worksphere.pigletherd.dto.request.CreatePigletHerdRequest;
import com.hainam.worksphere.pigletherd.dto.request.UpdatePigletHerdRequest;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdResponse;
import com.hainam.worksphere.pigletherd.mapper.PigletHerdMapper;
import com.hainam.worksphere.pigletherd.repository.PigletHerdRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.PigNotFoundException;
import com.hainam.worksphere.shared.exception.PigletHerdNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PigletHerdService {

    private final PigletHerdRepository pigletHerdRepository;
    private final PigRepository pigRepository;
    private final PigletHerdMapper pigletHerdMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIGLET_HERD")
    public PigletHerdResponse create(CreatePigletHerdRequest request, UUID createdBy) {
        if (pigletHerdRepository.existsActiveByHerdCode(request.getHerdCode())) {
            throw new BusinessRuleViolationException("Herd code already exists: " + request.getHerdCode());
        }

        PigletHerd herd = PigletHerd.builder()
                .herdCode(request.getHerdCode())
                .mother(findPigOrNull(request.getMotherId()))
                .father(findPigOrNull(request.getFatherId()))
                .quantity(request.getQuantity())
                .genderNote(request.getGenderNote())
                .averageBirthWeight(request.getAverageBirthWeight())
                .birthDate(request.getBirthDate())
                .createdBy(createdBy)
                .build();

        PigletHerd saved = pigletHerdRepository.save(herd);
        AuditContext.registerCreated(saved);
        return pigletHerdMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PigletHerdResponse> getAll() {
        return pigletHerdRepository.findAllActive().stream().map(pigletHerdMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public PigletHerdResponse getById(UUID id) {
        PigletHerd herd = pigletHerdRepository.findActiveById(id)
                .orElseThrow(() -> PigletHerdNotFoundException.byId(id.toString()));
        return pigletHerdMapper.toResponse(herd);
    }

    @Transactional(readOnly = true)
    public List<PigletHerdResponse> getByMotherId(UUID motherId) {
        return pigletHerdRepository.findActiveByMotherId(motherId).stream().map(pigletHerdMapper::toResponse).toList();
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PIGLET_HERD")
    public PigletHerdResponse update(UUID id, UpdatePigletHerdRequest request, UUID updatedBy) {
        PigletHerd herd = pigletHerdRepository.findActiveById(id)
                .orElseThrow(() -> PigletHerdNotFoundException.byId(id.toString()));

        AuditContext.snapshot(herd);

        if (request.getMotherId() != null) herd.setMother(findPigOrNull(request.getMotherId()));
        if (request.getFatherId() != null) herd.setFather(findPigOrNull(request.getFatherId()));
        if (request.getQuantity() != null) herd.setQuantity(request.getQuantity());
        if (request.getGenderNote() != null) herd.setGenderNote(request.getGenderNote());
        if (request.getAverageBirthWeight() != null) herd.setAverageBirthWeight(request.getAverageBirthWeight());
        if (request.getBirthDate() != null) herd.setBirthDate(request.getBirthDate());
        herd.setUpdatedBy(updatedBy);

        PigletHerd saved = pigletHerdRepository.save(herd);
        AuditContext.registerUpdated(saved);
        return pigletHerdMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "PIGLET_HERD")
    public void delete(UUID id, UUID deletedBy) {
        PigletHerd herd = pigletHerdRepository.findActiveById(id)
                .orElseThrow(() -> PigletHerdNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(herd);

        herd.setIsDeleted(true);
        herd.setDeletedAt(Instant.now());
        herd.setDeletedBy(deletedBy);
        pigletHerdRepository.save(herd);
    }

    private Pig findPigOrNull(UUID pigId) {
        if (pigId == null) return null;
        return pigRepository.findActiveById(pigId)
                .orElseThrow(() -> PigNotFoundException.byId(pigId.toString()));
    }
}
