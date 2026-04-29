package com.hainam.worksphere.pig.controller;

import com.hainam.worksphere.pig.dto.response.FarrowingHistoryItemResponse;
import com.hainam.worksphere.pig.dto.response.PregnantPigResponse;
import com.hainam.worksphere.pig.dto.response.PigWithLatestGrowthResponse;
import com.hainam.worksphere.pig.dto.response.SowResponse;
import com.hainam.worksphere.pig.service.PigQueryService;
import com.hainam.worksphere.reproductioncycle.domain.ReproductionCycle;
import com.hainam.worksphere.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pigs")
@RequiredArgsConstructor
@Tag(name = "Pig Management")
@SecurityRequirement(name = "Bearer Authentication")
public class PigQueryController {

    private final PigQueryService pigQueryService;

    /**
     * Danh sách lợn kèm thông tin bản ghi tăng trưởng gần nhất.
     * ?type=SOW|BOAR|MEAT|... để filter theo loại
     */
    @GetMapping("/with-latest-growth")
    @Operation(summary = "Danh sách lợn kèm bản ghi tăng trưởng gần nhất")
    public ResponseEntity<ApiResponse<List<PigWithLatestGrowthResponse>>> getAllWithLatestGrowth(
            @RequestParam(required = false) String type
    ) {
        List<PigWithLatestGrowthResponse> result = (type != null && !type.isBlank())
                ? pigQueryService.getAllWithLatestGrowthByType(type)
                : pigQueryService.getAllWithLatestGrowth();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * Danh sách lợn nái với thông tin sinh sản.
     * Thông tin: id, Số tai, Loại, giống, Ngày đẻ gần nhất, số lần mang thai, số lần sẩy thai, trạng thái
     */
    @GetMapping("/sows")
    @Operation(summary = "Danh sách lợn nái (SOW) kèm thông tin sinh sản")
    public ResponseEntity<ApiResponse<List<SowResponse>>> getAllSows() {
        return ResponseEntity.ok(ApiResponse.success(pigQueryService.getAllSows()));
    }

    /**
     * Lịch sử lứa đẻ của lợn nái theo pigId.
     * Trả về: Ngày đẻ, số lượng, số chết khô, số đè chết, số dị tật, số sống sót, cân nặng trung bình
     */
    @GetMapping("/{id}/farrowing-history")
    @Operation(summary = "Lịch sử lứa đẻ của lợn nái")
    public ResponseEntity<ApiResponse<List<FarrowingHistoryItemResponse>>> getFarrowingHistory(
            @PathVariable UUID id
    ) {
        List<ReproductionCycle> cycles = pigQueryService.getFarrowingHistoryBySowPigId(id);
        List<FarrowingHistoryItemResponse> result = cycles.stream()
                .map(rc -> FarrowingHistoryItemResponse.builder()
                        .cycleId(rc.getId())
                        .actualFarrowDate(rc.getActualFarrowDate())
                        .bornCount(rc.getBornCount())
                        .deadCount(rc.getDeadCount())
                        .crushedCount(rc.getCrushedCount())
                        .deformedCount(rc.getDeformedCount())
                        .aliveCount(rc.getAliveCount())
                        .averageWeight(rc.getAverageWeight())
                        .status(rc.getStatus())
                        .build())
                .toList();
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    /**
     * Danh sách lợn đang mang thai.
     * Trả về: id, số tai, ngày phối giống, ngày ghi nhận thai, ngày đẻ dự kiến, mang thai lần thứ, trạng thái
     */
    @GetMapping("/pregnant")
    @Operation(summary = "Danh sách lợn đang mang thai")
    public ResponseEntity<ApiResponse<List<PregnantPigResponse>>> getAllPregnant() {
        return ResponseEntity.ok(ApiResponse.success(pigQueryService.getAllPregnant()));
    }
}
