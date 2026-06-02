package com.hainam.worksphere.penpig.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferPenPigRequest {
    private UUID pigId;
    private List<UUID> pigIds;
    private UUID herdId;
    private String targetPenCode;
}
