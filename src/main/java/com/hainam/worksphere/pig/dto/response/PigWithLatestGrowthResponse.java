package com.hainam.worksphere.pig.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Response cho API danh sách lợn kèm thông tin bản ghi tăng trưởng gần nhất.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PigWithLatestGrowthResponse {

    // Thông tin lợn
    private UUID id;
    private String earTag;
    private String type;
    private String species;
    private String status;

    // Bản ghi tăng trưởng gần nhất
    private LocalDate latestTrackingDate;
    private Double weight;
    private Double litterLength;
    private Double chestGirth;
    private Double adg;
    private Double fcr;
}
