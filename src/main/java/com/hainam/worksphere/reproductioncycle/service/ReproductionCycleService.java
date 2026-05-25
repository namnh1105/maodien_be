package com.hainam.worksphere.reproductioncycle.service;

import com.hainam.worksphere.mating.domain.Mating;
import com.hainam.worksphere.mating.repository.MatingRepository;
import com.hainam.worksphere.penpig.domain.PenPig;
import com.hainam.worksphere.penpig.repository.PenPigRepository;
import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.pigletherd.domain.PigletHerd;
import com.hainam.worksphere.pigletherd.domain.PigletHerdStatus;
import com.hainam.worksphere.pigletherd.repository.PigletHerdRepository;
import com.hainam.worksphere.pigsemen.domain.PigSemen;
import com.hainam.worksphere.pigsemen.repository.PigSemenRepository;
import com.hainam.worksphere.mating.domain.MatingStatus;
import com.hainam.worksphere.reproductioncycle.domain.ReproductionCycle;
import com.hainam.worksphere.reproductioncycle.domain.ReproductionCycleStatus;
import com.hainam.worksphere.reproductioncycle.dto.request.CreateReproductionCycleRequest;
import com.hainam.worksphere.reproductioncycle.dto.request.RecordFarrowingRequest;
import com.hainam.worksphere.reproductioncycle.dto.request.UpdateReproductionCycleRequest;
import com.hainam.worksphere.reproductioncycle.dto.request.UpdateReproductionCycleStatusRequest;
import com.hainam.worksphere.reproductioncycle.dto.response.ReproductionCycleResponse;
import com.hainam.worksphere.reproductioncycle.mapper.ReproductionCycleMapper;
import com.hainam.worksphere.reproductioncycle.repository.ReproductionCycleRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReproductionCycleService {

    private final ReproductionCycleRepository reproductionCycleRepository;
    private final ReproductionCycleMapper reproductionCycleMapper;
    private final MatingRepository matingRepository;
    private final PigRepository pigRepository;
    private final PigSemenRepository pigSemenRepository;
    private final PigletHerdRepository pigletHerdRepository;
    private final PenPigRepository penPigRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "REPRODUCTION_CYCLE")
    public ReproductionCycleResponse create(CreateReproductionCycleRequest request, UUID createdBy) {
        String normalizedStatus = request.getStatus() != null
            ? normalizeCycleStatus(request.getStatus())
            : null;
        ReproductionCycle cycle = ReproductionCycle.builder()
                .matingId(request.getMatingId())
                .conceptionDate(request.getConceptionDate())
                .expectedFarrowDate(request.getExpectedFarrowDate())
                .actualFarrowDate(request.getActualFarrowDate())
            .status(normalizedStatus)
                .bornCount(request.getBornCount())
                .aliveCount(request.getAliveCount())
                .deadCount(request.getDeadCount())
                .crushedCount(request.getCrushedCount())
                .deformedCount(request.getDeformedCount())
                .averageWeight(request.getAverageWeight())
                .createdBy(createdBy)
                .build();

        ReproductionCycle saved = reproductionCycleRepository.save(cycle);
        AuditContext.registerCreated(saved);
        return toResponseWithEarTag(saved);
    }

    @Transactional(readOnly = true)
    public List<ReproductionCycleResponse> getAll() {
        return getAll(null);
    }

    @Transactional(readOnly = true)
    public List<ReproductionCycleResponse> getAll(String status) {
        List<ReproductionCycle> cycles = (status == null || status.isBlank())
            ? reproductionCycleRepository.findAllActive()
            : reproductionCycleRepository.findActiveByStatus(status);
        return cycles.stream().map(this::toResponseWithEarTag).toList();
    }

    @Transactional(readOnly = true)
    public ReproductionCycleResponse getById(UUID id) {
        ReproductionCycle cycle = reproductionCycleRepository.findActiveById(id)
            .orElseThrow(() -> new ResourceNotFoundException("ReproductionCycle", id.toString()));
        return toResponseWithEarTag(cycle);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "REPRODUCTION_CYCLE")
    public ReproductionCycleResponse update(UUID id, UpdateReproductionCycleRequest request, UUID updatedBy) {
        ReproductionCycle cycle = reproductionCycleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReproductionCycle", id.toString()));

        AuditContext.snapshot(cycle);

        if (request.getMatingId() != null) cycle.setMatingId(request.getMatingId());
        if (request.getConceptionDate() != null) cycle.setConceptionDate(request.getConceptionDate());
        if (request.getExpectedFarrowDate() != null) cycle.setExpectedFarrowDate(request.getExpectedFarrowDate());
        if (request.getActualFarrowDate() != null) cycle.setActualFarrowDate(request.getActualFarrowDate());
        if (request.getStatus() != null) cycle.setStatus(normalizeCycleStatus(request.getStatus()));
        if (request.getBornCount() != null) cycle.setBornCount(request.getBornCount());
        if (request.getAliveCount() != null) cycle.setAliveCount(request.getAliveCount());
        if (request.getDeadCount() != null) cycle.setDeadCount(request.getDeadCount());
        if (request.getCrushedCount() != null) cycle.setCrushedCount(request.getCrushedCount());
        if (request.getDeformedCount() != null) cycle.setDeformedCount(request.getDeformedCount());
        if (request.getAverageWeight() != null) cycle.setAverageWeight(request.getAverageWeight());
        cycle.setUpdatedBy(updatedBy);

        ReproductionCycle saved = reproductionCycleRepository.save(cycle);
        AuditContext.registerUpdated(saved);
        return toResponseWithEarTag(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "REPRODUCTION_CYCLE", actionCode = "RECORD_MISCARRIAGE")
    public List<ReproductionCycleResponse> recordMiscarriages(
            List<UpdateReproductionCycleStatusRequest> requests,
            UUID updatedBy
    ) {
        return requests.stream().map(request -> {
            ReproductionCycle cycle = reproductionCycleRepository.findActiveById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("ReproductionCycle", request.getId().toString()));

            AuditContext.snapshot(cycle);

            String normalizedStatus = normalizeCycleStatus(request.getStatus());
            cycle.setStatus(normalizedStatus);
            cycle.setUpdatedBy(updatedBy);

            ReproductionCycle saved = reproductionCycleRepository.save(cycle);
            AuditContext.registerUpdated(saved);

            updateMatingStatusFromCycle(saved, normalizedStatus, updatedBy);
            return toResponseWithEarTag(saved);
        }).toList();
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "REPRODUCTION_CYCLE", actionCode = "RECORD_FARROWING")
    public List<ReproductionCycleResponse> recordFarrowings(
            List<RecordFarrowingRequest> requests,
            UUID updatedBy
    ) {
        return requests.stream().map(request -> {
            ReproductionCycle cycle = reproductionCycleRepository.findActiveById(request.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("ReproductionCycle", request.getId().toString()));

            AuditContext.snapshot(cycle);

            if (request.getActualFarrowDate() != null) cycle.setActualFarrowDate(request.getActualFarrowDate());
            if (request.getBornCount() != null) cycle.setBornCount(request.getBornCount());
            if (request.getAliveCount() != null) cycle.setAliveCount(request.getAliveCount());
            if (request.getDeadCount() != null) cycle.setDeadCount(request.getDeadCount());
            if (request.getCrushedCount() != null) cycle.setCrushedCount(request.getCrushedCount());
            if (request.getDeformedCount() != null) cycle.setDeformedCount(request.getDeformedCount());
            if (request.getAverageWeight() != null) cycle.setAverageWeight(request.getAverageWeight());

            String normalizedStatus = request.getStatus() != null
                    ? normalizeCycleStatus(request.getStatus())
                    : cycle.getStatus();
            if (normalizedStatus != null) {
                cycle.setStatus(normalizedStatus);
            }
            cycle.setUpdatedBy(updatedBy);

            ReproductionCycle saved = reproductionCycleRepository.save(cycle);
            AuditContext.registerUpdated(saved);

            updateMatingStatusFromCycle(saved, normalizedStatus, updatedBy);

            if (isFarrowingSuccessStatus(normalizedStatus)) {
                createPigletHerdIfAbsent(saved, request, updatedBy);
            }

            return toResponseWithEarTag(saved);
        }).toList();
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "REPRODUCTION_CYCLE")
    public void delete(UUID id, UUID deletedBy) {
        ReproductionCycle cycle = reproductionCycleRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ReproductionCycle", id.toString()));

        AuditContext.registerDeleted(cycle);

        cycle.setIsDeleted(true);
        cycle.setDeletedAt(Instant.now());
        cycle.setDeletedBy(deletedBy);
        reproductionCycleRepository.save(cycle);
    }

    private void updateMatingStatusFromCycle(ReproductionCycle cycle, String cycleStatus, UUID updatedBy) {
        if (cycle.getMatingId() == null || cycleStatus == null) {
            return;
        }

        matingRepository.findActiveById(cycle.getMatingId()).ifPresent(mating -> {
            AuditContext.snapshot(mating);

            String normalizedCycleStatus = normalizeCycleStatus(cycleStatus);
            if (ReproductionCycleStatus.MISCARRIED.name().equals(normalizedCycleStatus)) {
                mating.setStatus(MatingStatus.FAILURE.name());
            } else if (ReproductionCycleStatus.FARROWED.name().equals(normalizedCycleStatus)) {
                mating.setStatus(MatingStatus.SUCCESS.name());
            } else if (ReproductionCycleStatus.TRACKING.name().equals(normalizedCycleStatus)) {
                mating.setStatus(MatingStatus.SUCCESS.name());
            } else {
                mating.setStatus(cycleStatus);
            }

            mating.setUpdatedBy(updatedBy);
            matingRepository.save(mating);
        });
    }

    private void createPigletHerdIfAbsent(ReproductionCycle cycle, RecordFarrowingRequest request, UUID createdBy) {
        Mating mating = matingRepository.findActiveById(cycle.getMatingId())
                .orElseThrow(() -> new ResourceNotFoundException("Mating", cycle.getMatingId().toString()));

        Pig mother = pigRepository.findActiveById(mating.getSowPigId())
                .orElseThrow(() -> new ResourceNotFoundException("Pig", mating.getSowPigId().toString()));

        Pig father = null;
        if (mating.getSemenId() != null) {
            PigSemen semen = pigSemenRepository.findActiveById(mating.getSemenId()).orElse(null);
            if (semen != null && semen.getBoarPigId() != null) {
                father = pigRepository.findActiveById(semen.getBoarPigId()).orElse(null);
            }
        }

        LocalDate birthDate = request.getActualFarrowDate() != null
                ? request.getActualFarrowDate()
                : LocalDate.now();

        int litterNumber = reproductionCycleRepository.findActiveBySowPigId(mother.getId()).size();

        if (pigletHerdRepository.existsActiveByMotherIdAndLitterNumberAndBirthDate(
                mother.getId(), litterNumber, birthDate)) {
            return;
        }

        UUID penId = null;
        List<PenPig> currentAssignments = penPigRepository.findCurrentByPigId(mother.getId());
        if (!currentAssignments.isEmpty()) {
            penId = currentAssignments.get(0).getPenId();
        }

        String motherTag = mother.getEarTag() != null ? mother.getEarTag() : "";
        String herdName = motherTag.isBlank() ? null : motherTag + "-" + litterNumber;

        PigletHerd herd = PigletHerd.builder()
                .herdName(herdName)
                .litterNumber(litterNumber)
                .penId(penId)
                .mother(mother)
                .father(father)
                .quantity(request.getAliveCount())
                .averageBirthWeight(request.getAverageWeight())
                .birthDate(birthDate)
                .semenId(mating.getSemenId())
                .status(PigletHerdStatus.UNWEANED)
                .createdBy(createdBy)
                .build();

        PigletHerd saved = pigletHerdRepository.save(herd);
        AuditContext.registerCreated(saved);
    }

    private boolean isFarrowingSuccessStatus(String status) {
        if (status == null || status.isBlank()) return false;
        String normalized = normalizeText(status);
        if (normalized.contains("success") || normalized.contains("da de")
            || normalized.equals("de") || normalized.contains("farrow")) {
            return true;
        }
        String enumCandidate = status.trim().toUpperCase().replace(' ', '_');
        return ReproductionCycleStatus.FARROWED.name().equals(enumCandidate) || "FARROWED".equals(enumCandidate);
    }

    private String normalizeCycleStatus(String status) {
        if (status == null || status.isBlank()) return status;

        String normalized = normalizeText(status);
        if (normalized.contains("success") || normalized.contains("da de")
                || normalized.equals("de") || normalized.contains("farrow")) {
            return ReproductionCycleStatus.FARROWED.name();
        }

        if (normalized.contains("say") || normalized.contains("miscarriage")
                || normalized.contains("fail") || normalized.contains("aborted")) {
            return ReproductionCycleStatus.MISCARRIED.name();
        }

        if (normalized.contains("dang mang thai") || normalized.contains("mang thai")
                || normalized.contains("chua") || normalized.contains("dau thai")
                || normalized.contains("pregnant") || normalized.contains("theo doi")) {
            return ReproductionCycleStatus.TRACKING.name();
        }

        String enumCandidate = status.trim().toUpperCase().replace(' ', '_');
        try {
            return ReproductionCycleStatus.valueOf(enumCandidate).name();
        } catch (IllegalArgumentException ignored) {
            if ("PREGNANT".equals(enumCandidate)) return ReproductionCycleStatus.TRACKING.name();
            if ("FARROWED".equals(enumCandidate)) return ReproductionCycleStatus.FARROWED.name();
            if ("MISCARRIED".equals(enumCandidate)) return ReproductionCycleStatus.MISCARRIED.name();
            return status.trim();
        }
    }

    private String normalizeText(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.trim().toLowerCase();
    }

    private ReproductionCycleResponse toResponseWithEarTag(ReproductionCycle cycle) {
        ReproductionCycleResponse response = reproductionCycleMapper.toResponse(cycle);

        if (cycle.getMatingId() != null) {
            matingRepository.findActiveById(cycle.getMatingId()).ifPresent(mating -> {
                if (mating.getSowPigId() != null) {
                    pigRepository.findActiveById(mating.getSowPigId())
                            .ifPresent(pig -> response.setSowPigEarTag(pig.getEarTag()));
                }
            });
        }

        return response;
    }
}
