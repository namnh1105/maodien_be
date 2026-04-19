package com.hainam.worksphere.pen.service;

import com.hainam.worksphere.feedingration.domain.FeedingRation;
import com.hainam.worksphere.feedingration.repository.FeedingRationRepository;
import com.hainam.worksphere.pen.domain.Pen;
import com.hainam.worksphere.pen.dto.response.PenDetailResponse;
import com.hainam.worksphere.pen.dto.response.PenPigSummaryResponse;
import com.hainam.worksphere.pen.dto.response.PenPigletHerdSummaryResponse;
import com.hainam.worksphere.pen.repository.PenRepository;
import com.hainam.worksphere.penpig.domain.PenPig;
import com.hainam.worksphere.penpig.repository.PenPigRepository;
import com.hainam.worksphere.pig.domain.Pig;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.pigletherd.domain.PigletHerd;
import com.hainam.worksphere.pigletherd.repository.PigletHerdRepository;
import com.hainam.worksphere.shared.exception.PenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PenDetailService {

    private final PenRepository penRepository;
    private final PenPigRepository penPigRepository;
    private final PigRepository pigRepository;
    private final PigletHerdRepository pigletHerdRepository;
    private final FeedingRationRepository feedingRationRepository;

    @Transactional(readOnly = true)
    public PenDetailResponse getDetail(UUID penId) {
        Pen pen = penRepository.findActiveById(penId)
                .orElseThrow(() -> PenNotFoundException.byId(penId.toString()));

        List<PenPig> currentAssignments = penPigRepository.findCurrentByPenId(penId);
        List<UUID> pigIds = currentAssignments.stream().map(PenPig::getPigId).distinct().toList();

        Map<UUID, Pig> pigMap = pigIds.isEmpty()
                ? Map.of()
                : pigRepository.findActiveByIds(pigIds).stream().collect(Collectors.toMap(Pig::getId, p -> p));

        List<PenPigSummaryResponse> pigs = new ArrayList<>();
        for (PenPig assignment : currentAssignments) {
            Pig pig = pigMap.get(assignment.getPigId());
            if (pig == null) {
                continue;
            }
            pigs.add(PenPigSummaryResponse.builder()
                    .pigId(pig.getId())
                    .earTag(pig.getEarTag())
                    .currentWeight(pig.getBirthWeight())
                    .type(pig.getType() != null ? pig.getType().name() : null)
                    .status(pig.getStatus() != null ? pig.getStatus().name() : null)
                    .build());
        }

        List<PigletHerd> herds = pigIds.isEmpty() ? List.of() : pigletHerdRepository.findActiveByMotherIds(pigIds);
        List<PenPigletHerdSummaryResponse> pigletHerds = herds.stream()
                .map(h -> PenPigletHerdSummaryResponse.builder()
                        .herdId(h.getId())
                        .herdName(h.getHerdName())
                        .quantity(h.getQuantity())
                        .averageBirthWeight(h.getAverageBirthWeight())
                        .build())
                .toList();

        int pigletCount = herds.stream().map(PigletHerd::getQuantity).filter(q -> q != null).mapToInt(Integer::intValue).sum();

        List<FeedingRation> latestRations = feedingRationRepository.findActiveByPenIdOrderByLatest(penId);
        Double latestAverageIntake = latestRations.isEmpty() ? null : latestRations.get(0).getAverageIntake();

        return PenDetailResponse.builder()
                .id(pen.getId())
                .penCode(pen.getPenCode())
                .name(pen.getName())
                .areaId(pen.getAreaId())
                .area(pen.getArea())
                .pigCount(pigs.size())
                .pigletCount(pigletCount)
                .latestAverageIntake(latestAverageIntake)
                .pigs(pigs)
                .pigletHerds(pigletHerds)
                .build();
    }
}
