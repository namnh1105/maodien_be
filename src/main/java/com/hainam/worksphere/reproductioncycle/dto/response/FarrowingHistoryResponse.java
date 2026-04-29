package com.hainam.worksphere.reproductioncycle.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response lịch sử lứa đẻ của lợn nái (1 lứa = 1 ReproductionCycle).
 * Trả về cho API GET /api/v1/pigs/{id}/farrowing-history
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FarrowingHistoryResponse {

    private UUID cycleId;

    /** Ngày đẻ thực tế */
    private LocalDate actualFarrowDate;

    /** Tổng số con sinh ra */
    private Integer bornCount;

    /** Số con chết khô (stillborn) */
    private Integer deadCount;

    /** Số con đè chết (crushed) - lấy từ field note hoặc tính riêng */
    private Integer crushedCount;

    /** Số con dị tật */
    private Integer deformedCount;

    /** Số con sống sót */
    private Integer aliveCount;

    /** Cân nặng trung bình lúc sinh */
    private Double averageWeight;

    /** Trạng thái lứa đẻ */
    private String status;
}
