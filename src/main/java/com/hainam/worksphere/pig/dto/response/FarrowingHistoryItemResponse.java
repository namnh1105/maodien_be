package com.hainam.worksphere.pig.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response mỗi lứa đẻ của lợn nái.
 * Trả về cho API GET /api/v1/pigs/{id}/farrowing-history
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarrowingHistoryItemResponse {

    private UUID cycleId;

    /** Ngày đẻ thực tế */
    private LocalDate actualFarrowDate;

    /** Tổng số con sinh ra */
    private Integer bornCount;

    /** Số con chết khô (stillborn / dead at birth) */
    private Integer deadCount;

    /** Số con đè chết (crushed by mother) */
    private Integer crushedCount;

    /** Số con dị tật */
    private Integer deformedCount;

    /** Số con sống sót */
    private Integer aliveCount;

    /** Cân nặng trung bình lúc sinh (kg) */
    private Double averageWeight;

    /** Trạng thái lứa đẻ */
    private String status;
}
