package com.hainam.worksphere.pigimport.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PigImportInvoiceDetailPigResponse {
    private UUID id;
    private UUID detailId;
    private UUID pigId;
    private String earTag;
    private UUID penId;
}
