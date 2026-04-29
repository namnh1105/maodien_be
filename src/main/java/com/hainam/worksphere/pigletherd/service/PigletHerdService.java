package com.hainam.worksphere.pigletherd.service;

import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.pigletherd.domain.PigletHerd;
import com.hainam.worksphere.pigletherd.domain.PigletHerdMovement;
import com.hainam.worksphere.pigletherd.domain.PigletHerdMovementType;
import com.hainam.worksphere.pigletherd.dto.request.CreatePigletHerdRequest;
import com.hainam.worksphere.pigletherd.dto.request.MergePigletHerdRequest;
import com.hainam.worksphere.pigletherd.dto.request.SplitPigletHerdRequest;
import com.hainam.worksphere.pigletherd.dto.request.UpdatePigletHerdRequest;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdDetailResponse;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdGrowthResponse;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdMovementResponse;
import com.hainam.worksphere.pigletherd.dto.response.PigletHerdResponse;
import com.hainam.worksphere.pigletherd.mapper.PigletHerdGrowthMapper;
import com.hainam.worksphere.pigletherd.mapper.PigletHerdMapper;
import com.hainam.worksphere.pigletherd.mapper.PigletHerdMovementMapper;
import com.hainam.worksphere.pigletherd.repository.PigletHerdGrowthRepository;
import com.hainam.worksphere.pigletherd.repository.PigletHerdMovementRepository;
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
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PigletHerdService {

    private final PigletHerdRepository pigletHerdRepository;
    private final PigRepository pigRepository;
    private final PigletHerdMapper pigletHerdMapper;
    private final PigletHerdGrowthRepository pigletHerdGrowthRepository;
    private final PigletHerdMovementRepository pigletHerdMovementRepository;
    private final PigletHerdGrowthMapper pigletHerdGrowthMapper;
    private final PigletHerdMovementMapper pigletHerdMovementMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIGLET_HERD")
    public PigletHerdResponse create(CreatePigletHerdRequest request, UUID createdBy) {
        

        PigletHerd herd = PigletHerd.builder()
                
            
            .litterNumber(request.getLitterNumber())
                .mother(findPigOrNull(request.getMotherId()))
                .father(findPigOrNull(request.getFatherId()))
                .quantity(request.getQuantity())
                .genderNote(request.getGenderNote())
                .averageBirthWeight(request.getAverageBirthWeight())
                .birthDate(request.getBirthDate())
                .semenId(request.getSemenId())
                .status(request.getStatus())
                .createdBy(createdBy)
                .build();

        herd.setHerdName(buildHerdName(herd.getMother(), herd.getLitterNumber()));

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
        public PigletHerdDetailResponse getDetailById(UUID id) {
        PigletHerd herd = pigletHerdRepository.findActiveById(id)
            .orElseThrow(() -> PigletHerdNotFoundException.byId(id.toString()));

        List<PigletHerdGrowthResponse> growthHistory = pigletHerdGrowthRepository.findActiveByHerdId(id)
            .stream()
            .map(pigletHerdGrowthMapper::toResponse)
            .toList();

        List<PigletHerdMovementResponse> movementHistory = pigletHerdMovementRepository.findActiveByHerdId(id)
            .stream()
            .map(pigletHerdMovementMapper::toResponse)
            .toList();

        return PigletHerdDetailResponse.builder()
            .herd(pigletHerdMapper.toResponse(herd))
            .growthHistory(growthHistory)
            .movementHistory(movementHistory)
            .build();
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

        
        if (request.getLitterNumber() != null) herd.setLitterNumber(request.getLitterNumber());
        if (request.getMotherId() != null) herd.setMother(findPigOrNull(request.getMotherId()));
        if (request.getFatherId() != null) herd.setFather(findPigOrNull(request.getFatherId()));
        if (request.getQuantity() != null) herd.setQuantity(request.getQuantity());
        if (request.getGenderNote() != null) herd.setGenderNote(request.getGenderNote());
        if (request.getAverageBirthWeight() != null) herd.setAverageBirthWeight(request.getAverageBirthWeight());
        if (request.getBirthDate() != null) herd.setBirthDate(request.getBirthDate());
        if (request.getSemenId() != null) herd.setSemenId(request.getSemenId());
        if (request.getStatus() != null) herd.setStatus(request.getStatus());
        herd.setHerdName(buildHerdName(herd.getMother(), herd.getLitterNumber()));
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

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PIGLET_HERD")
    public PigletHerdResponse splitHerd(SplitPigletHerdRequest request, UUID updatedBy) {
        PigletHerd source = pigletHerdRepository.findActiveById(request.getSourceHerdId())
                .orElseThrow(() -> PigletHerdNotFoundException.byId(request.getSourceHerdId().toString()));

        
        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BusinessRuleViolationException("Split quantity must be greater than 0");
        }
        if (source.getQuantity() == null || source.getQuantity() < request.getQuantity()) {
            throw new BusinessRuleViolationException("Split quantity exceeds source herd quantity");
        }

        AuditContext.snapshot(source);

        source.setQuantity(source.getQuantity() - request.getQuantity());
        source.setUpdatedBy(updatedBy);
        pigletHerdRepository.save(source);

        PigletHerd target = PigletHerd.builder()
                
                
                .litterNumber(request.getLitterNumber())
                .mother(source.getMother())
                .father(source.getFather())
                .quantity(request.getQuantity())
                .genderNote(source.getGenderNote())
                .averageBirthWeight(source.getAverageBirthWeight())
                .birthDate(source.getBirthDate())
                .createdBy(updatedBy)
                .build();
        target.setHerdName(buildHerdName(target.getMother(), target.getLitterNumber()));
        PigletHerd savedTarget = pigletHerdRepository.save(target);

        saveMovement(
                source.getId(),
                PigletHerdMovementType.SPLIT,
                source.getId(),
                savedTarget.getId(),
                request.getMovementDate(),
                request.getQuantity(),
                request.getReason(),
                updatedBy
        );

        AuditContext.registerUpdated(source);
        AuditContext.registerCreated(savedTarget);
        return pigletHerdMapper.toResponse(savedTarget);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PIGLET_HERD")
    public PigletHerdResponse mergeHerd(MergePigletHerdRequest request, UUID updatedBy) {
        PigletHerd target = pigletHerdRepository.findActiveById(request.getTargetHerdId())
                .orElseThrow(() -> PigletHerdNotFoundException.byId(request.getTargetHerdId().toString()));

        int totalAdded = 0;
        for (UUID sourceId : request.getSourceHerdIds()) {
            if (sourceId.equals(target.getId())) {
                continue;
            }

            PigletHerd source = pigletHerdRepository.findActiveById(sourceId)
                    .orElseThrow(() -> PigletHerdNotFoundException.byId(sourceId.toString()));

            int quantity = source.getQuantity() == null ? 0 : source.getQuantity();
            if (quantity <= 0) {
                continue;
            }

            AuditContext.snapshot(source);
            source.setQuantity(0);
            source.setUpdatedBy(updatedBy);
            pigletHerdRepository.save(source);

            totalAdded += quantity;

            saveMovement(
                    target.getId(),
                    PigletHerdMovementType.MERGE,
                    source.getId(),
                    target.getId(),
                    request.getMovementDate(),
                    quantity,
                    request.getReason(),
                    updatedBy
            );

            AuditContext.registerUpdated(source);
        }

        AuditContext.snapshot(target);
        target.setQuantity((target.getQuantity() == null ? 0 : target.getQuantity()) + totalAdded);
        target.setUpdatedBy(updatedBy);
        PigletHerd savedTarget = pigletHerdRepository.save(target);
        AuditContext.registerUpdated(savedTarget);

        return pigletHerdMapper.toResponse(savedTarget);
    }

    private Pig findPigOrNull(UUID pigId) {
        if (pigId == null) return null;
        return pigRepository.findActiveById(pigId)
                .orElseThrow(() -> PigNotFoundException.byId(pigId.toString()));
    }

    private String buildHerdName(Pig mother, Integer litterNumber) {
        if (mother == null || litterNumber == null) {
            return null;
        }
        String motherTag = mother.getEarTag() != null && !mother.getEarTag().isBlank()
                ? mother.getEarTag()
                : "";
        return motherTag + "-" + litterNumber;
    }

    private void saveMovement(
            UUID herdId,
            PigletHerdMovementType movementType,
            UUID sourceHerdId,
            UUID targetHerdId,
            LocalDate movementDate,
            Integer quantity,
            String reason,
            UUID createdBy
    ) {
        PigletHerdMovement movement = PigletHerdMovement.builder()
                .herdId(herdId)
                .movementType(movementType)
                .sourceHerdId(sourceHerdId)
                .targetHerdId(targetHerdId)
                .movementDate(movementDate)
                .quantity(quantity)
                .reason(reason)
                .createdBy(createdBy)
                .build();
        pigletHerdMovementRepository.save(movement);
    }
}
