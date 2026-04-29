package com.hainam.worksphere.pig.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response cho API danh sách lợn đang mang thai.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PregnantPigResponse {

    private UUID id;
    private String earTag;

    /** Ngày phối giống (matingDate từ mating_records) */
    private LocalDate matingDate;

    /** Ngày ghi nhận thai (conceptionDate từ reproduction_cycles) */
    private LocalDate conceptionDate;

    /** Ngày đẻ dự kiến (expectedFarrowDate từ reproduction_cycles) */
    private LocalDate expectedFarrowDate;

    /** Mang thai lần thứ (tổng số lần mang thai tính đến hiện tại) */
    private Integer pregnancyNumber;

    /** Trạng thái (từ reproduction_cycles.status) */
    private String status;
}
