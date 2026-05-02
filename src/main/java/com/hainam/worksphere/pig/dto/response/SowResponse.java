package com.hainam.worksphere.pig.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response cho API danh sách lợn nái (SOW).
 * Bao gồm thông tin sinh sản: ngày đẻ gần nhất, số lần mang thai, số lần sẩy thai.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SowResponse {

    private UUID id;
    private String earTag;
    private String type;
    private String species;
    private String breedName;

    /** Ngày đẻ gần nhất (actualFarrowDate từ reproduction_cycles) */
    private LocalDate lastFarrowDate;

    /** Tổng số lần mang thai (số records trong reproduction_cycles) */
    private Integer totalPregnancies;

    /** Số lần sẩy thai / đẻ thất bại (status = FAILED/ABORTED trong reproduction_cycles) */
    private Integer miscarriageCount;

    private String status;
}
