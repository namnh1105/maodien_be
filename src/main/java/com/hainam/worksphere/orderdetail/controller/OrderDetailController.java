package com.hainam.worksphere.orderdetail.controller;

import com.hainam.worksphere.auth.security.UserPrincipal;
import com.hainam.worksphere.orderdetail.dto.request.CreateOrderDetailRequest;
import com.hainam.worksphere.orderdetail.dto.request.UpdateOrderDetailRequest;
import com.hainam.worksphere.orderdetail.dto.response.OrderDetailResponse;
import com.hainam.worksphere.orderdetail.service.OrderDetailService;
import com.hainam.worksphere.shared.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/order-details")
@RequiredArgsConstructor
@Tag(name = "OrderDetail Management")
@SecurityRequirement(name = "Bearer Authentication")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping
    @Operation(summary = "Create order detail")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> create(
            @Valid @RequestBody CreateOrderDetailRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        OrderDetailResponse response = orderDetailService.create(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Order detail created successfully", response));
    }

    @GetMapping
    @Operation(summary = "Get all order details")
    public ResponseEntity<ApiResponse<List<OrderDetailResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(orderDetailService.getAll()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order detail by id")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(orderDetailService.getById(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update order detail")
    public ResponseEntity<ApiResponse<OrderDetailResponse>> update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOrderDetailRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        OrderDetailResponse response = orderDetailService.update(id, request, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Order detail updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete order detail")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable UUID id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        orderDetailService.delete(id, userPrincipal.getId());
        return ResponseEntity.ok(ApiResponse.success("Order detail deleted successfully", null));
    }
}
