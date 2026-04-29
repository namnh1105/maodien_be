package com.hainam.worksphere.pigsemen.service;

import com.hainam.worksphere.pigsemen.domain.PigSemen;
import com.hainam.worksphere.pigsemen.dto.request.CreatePigSemenRequest;
import com.hainam.worksphere.pigsemen.dto.response.PigSemenResponse;
import com.hainam.worksphere.pigsemen.mapper.PigSemenMapper;
import com.hainam.worksphere.pigsemen.repository.PigSemenRepository;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PigSemenService {

    private final PigSemenRepository pigSemenRepository;
    private final PigSemenMapper pigSemenMapper;
    private final PigRepository pigRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "PIG_SEMEN")
    public PigSemenResponse create(CreatePigSemenRequest request, UUID createdBy) {
        PigSemen entity = PigSemen.builder()
                .code(request.getCode())
                .boarPigId(request.getBoarPigId())
                .boarBreed(request.getBoarBreed())
                .collectionDate(request.getCollectionDate())
                .volume(request.getVolume())
                .motility(request.getMotility())
                .quality(request.getQuality())
                .status(request.getStatus())
                .note(request.getNote())
                .createdBy(createdBy)
                .build();

        PigSemen saved = pigSemenRepository.save(entity);
        AuditContext.registerCreated(saved);
        
        return toResponseWithEarTag(saved);
    }

    private PigSemenResponse toResponseWithEarTag(PigSemen pigSemen) {
        PigSemenResponse response = pigSemenMapper.toResponse(pigSemen);
        if (pigSemen.getBoarPigId() != null) {
            pigRepository.findActiveById(pigSemen.getBoarPigId())
                    .ifPresent(pig -> response.setBoarPigEarTag(pig.getEarTag()));
        }
        return response;
    }
}
