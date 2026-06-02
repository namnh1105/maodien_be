package com.hainam.worksphere.pig.service;

import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.domain.PigStatus;
import com.hainam.worksphere.pig.domain.PigType;
import com.hainam.worksphere.pig.dto.request.CreatePigRequest;
import com.hainam.worksphere.pig.dto.request.UpdatePigRequest;
import com.hainam.worksphere.pig.dto.response.PigResponse;
import com.hainam.worksphere.pig.mapper.PigMapper;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.growthtracking.domain.GrowthTracking;
import com.hainam.worksphere.growthtracking.repository.GrowthTrackingRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.BusinessRuleViolationException;
import com.hainam.worksphere.shared.exception.PigNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PigService {

    private final PigRepository pigRepository;
    private final PigMapper pigMapper;
    private final com.hainam.worksphere.breed.repository.BreedRepository breedRepository;
    private final GrowthTrackingRepository growthTrackingRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIG")
    public PigResponse create(CreatePigRequest request, UUID createdBy) {
        String earTag = request.getEarTag();
        if (earTag != null && !earTag.isBlank()) {
            String normalizedEarTag = earTag.trim();
            if (pigRepository.findActiveByEarTag(normalizedEarTag).isPresent()) {
                throw new BusinessRuleViolationException("Mã tai đã tồn tại");
            }
            earTag = normalizedEarTag;
        }
        Pig pig = Pig.builder()
                .earTag(earTag)
                .birthWeight(request.getBirthWeight())
                .birthDate(request.getBirthDate())
                .type(parsePigType(request.getType()))
                .origin(request.getOrigin())
                .species(request.getSpecies())
                .nippleCount(request.getNippleCount())
                .herdEntryDate(request.getHerdEntryDate())
                .status(parsePigStatus(request.getStatus()))
                .createdBy(createdBy)
                .build();

        Pig saved = pigRepository.save(pig);
        AuditContext.registerCreated(saved);
        return toResponseWithBreedName(saved);
    }

    @Transactional(readOnly = true)
    public List<PigResponse> getAll() {
        return buildResponsesWithLatestWeight(pigRepository.findAllActive());
    }

    @Transactional(readOnly = true)
    public PigResponse getById(UUID id) {
        Pig pig = pigRepository.findActiveById(id)
                .orElseThrow(() -> PigNotFoundException.byId(id.toString()));
        PigResponse response = toResponseWithBreedName(pig);
        response.setCurrentWeight(getLatestWeight(pig.getId(), pig.getBirthWeight()));
        return response;
    }

    @Transactional(readOnly = true)
    public List<PigResponse> getByStatus(String status) {
        PigStatus parsedStatus = parsePigStatus(status);
        return buildResponsesWithLatestWeight(pigRepository.findActiveByStatus(parsedStatus));
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "PIG")
    public PigResponse update(UUID id, UpdatePigRequest request, UUID updatedBy) {
        Pig pig = pigRepository.findActiveById(id)
                .orElseThrow(() -> PigNotFoundException.byId(id.toString()));

        AuditContext.snapshot(pig);

        if (request.getEarTag() != null) pig.setEarTag(request.getEarTag());
        if (request.getBirthWeight() != null) pig.setBirthWeight(request.getBirthWeight());
        if (request.getBirthDate() != null) pig.setBirthDate(request.getBirthDate());
        if (request.getType() != null) pig.setType(parsePigType(request.getType()));
        if (request.getOrigin() != null) pig.setOrigin(request.getOrigin());
        if (request.getSpecies() != null) pig.setSpecies(request.getSpecies());
        if (request.getNippleCount() != null) pig.setNippleCount(request.getNippleCount());
        if (request.getHerdEntryDate() != null) pig.setHerdEntryDate(request.getHerdEntryDate());
        if (request.getStatus() != null) pig.setStatus(parsePigStatus(request.getStatus()));
        pig.setUpdatedBy(updatedBy);

        Pig saved = pigRepository.save(pig);
        AuditContext.registerUpdated(saved);
        return toResponseWithBreedName(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "PIG")
    public void delete(UUID id, UUID deletedBy) {
        Pig pig = pigRepository.findActiveById(id)
                .orElseThrow(() -> PigNotFoundException.byId(id.toString()));

        AuditContext.registerDeleted(pig);

        pig.setIsDeleted(true);
        pig.setDeletedAt(Instant.now());
        pig.setDeletedBy(deletedBy);
        pigRepository.save(pig);
    }

    private PigType parsePigType(String rawType) {
        if (rawType == null || rawType.isBlank()) {
            throw new BusinessRuleViolationException("Pig type is required");
        }
        try {
            String normalized = normalizeText(rawType)
                    .replace('-', ' ')
                    .replace('_', ' ')
                    .replaceAll("\\s+", " ")
                    .trim()
                    .toUpperCase();
            String enumCandidate = normalized.replace(' ', '_');
            return PigType.valueOf(enumCandidate);
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleViolationException("Invalid pig type: " + rawType);
        }
    }

    private String normalizeText(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{M}", "");
        return normalized.trim().toLowerCase();
    }

    private String generatePigCode(UUID id) {
        String shortId = id.toString().replace("-", "").substring(0, 8).toUpperCase();
        return "PIG-" + shortId;
    }

    private PigStatus parsePigStatus(String rawStatus) {
        if (rawStatus == null || rawStatus.isBlank()) {
            return PigStatus.ACTIVE;
        }
        try {
            return PigStatus.valueOf(rawStatus.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleViolationException("Invalid pig status: " + rawStatus);
        }
    }

    private PigResponse toResponseWithBreedName(Pig pig) {
        PigResponse response = pigMapper.toResponse(pig);
        response.setBreedName(resolveBreedName(pig.getSpecies()));
        return response;
    }

    private List<PigResponse> buildResponsesWithLatestWeight(List<Pig> pigs) {
        if (pigs.isEmpty()) return List.of();

        List<UUID> pigIds = pigs.stream().map(Pig::getId).toList();
        List<GrowthTracking> allGrowths = growthTrackingRepository.findActiveByPigIds(pigIds);

        Map<UUID, GrowthTracking> latestGrowthByPigId = allGrowths.stream()
                .collect(Collectors.toMap(
                        GrowthTracking::getPigId,
                        g -> g,
                        (g1, g2) -> {
                            if (g1.getTrackingDate() == null) return g2;
                            if (g2.getTrackingDate() == null) return g1;
                            return g1.getTrackingDate().isAfter(g2.getTrackingDate()) ? g1 : g2;
                        }
                ));

        return pigs.stream().map(pig -> {
            PigResponse response = toResponseWithBreedName(pig);
            GrowthTracking latest = latestGrowthByPigId.get(pig.getId());
            Double currentWeight = latest != null && latest.getWeight() != null
                    ? latest.getWeight()
                    : pig.getBirthWeight();
            response.setCurrentWeight(currentWeight);
            return response;
        }).toList();
    }

    private Double getLatestWeight(UUID pigId, Double fallbackWeight) {
        if (pigId == null) return fallbackWeight;
        List<GrowthTracking> growths = growthTrackingRepository.findActiveByPigId(pigId);
        if (growths.isEmpty()) return fallbackWeight;
        return growths.stream()
                .filter(g -> g.getWeight() != null)
                .max(Comparator.comparing(GrowthTracking::getTrackingDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .map(GrowthTracking::getWeight)
                .orElse(fallbackWeight);
    }

    private String resolveBreedName(String species) {
        if (species == null || species.isBlank()) return null;
        try {
            UUID breedId = UUID.fromString(species);
            return breedRepository.findActiveById(breedId)
                    .map(com.hainam.worksphere.breed.domain.Breed::getName)
                    .orElseGet(() -> breedRepository.findActiveByCode(species)
                            .map(com.hainam.worksphere.breed.domain.Breed::getName)
                            .orElseGet(() -> breedRepository.findActiveByName(species)
                                    .map(com.hainam.worksphere.breed.domain.Breed::getName)
                                    .orElse(null)));
        } catch (IllegalArgumentException ex) {
            return breedRepository.findActiveByCode(species)
                    .map(com.hainam.worksphere.breed.domain.Breed::getName)
                    .orElseGet(() -> breedRepository.findActiveByName(species)
                            .map(com.hainam.worksphere.breed.domain.Breed::getName)
                            .orElse(null));
        }
    }
}
