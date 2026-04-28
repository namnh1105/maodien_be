package com.hainam.worksphere.orderdetail.service;

import com.hainam.worksphere.orderdetail.domain.OrderDetail;
import com.hainam.worksphere.orderdetail.dto.request.CreateOrderDetailRequest;
import com.hainam.worksphere.orderdetail.dto.request.UpdateOrderDetailRequest;
import com.hainam.worksphere.orderdetail.dto.response.OrderDetailResponse;
import com.hainam.worksphere.orderdetail.mapper.OrderDetailMapper;
import com.hainam.worksphere.orderdetail.repository.OrderDetailRepository;
import com.hainam.worksphere.pig.repository.PigRepository;
import com.hainam.worksphere.shared.audit.annotation.AuditAction;
import com.hainam.worksphere.shared.audit.domain.ActionType;
import com.hainam.worksphere.shared.audit.util.AuditContext;
import com.hainam.worksphere.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final PigRepository pigRepository;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "ORDER_DETAIL")
    public OrderDetailResponse create(CreateOrderDetailRequest request, UUID createdBy) {
        OrderDetail orderDetail = orderDetailMapper.toEntity(request);
        orderDetail.setCreatedBy(createdBy);

        OrderDetail saved = orderDetailRepository.save(orderDetail);
        AuditContext.registerCreated(saved);
        return toResponseWithEarTag(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderDetailResponse> getAll() {
        return orderDetailRepository.findAllActive().stream()
                .map(this::toResponseWithEarTag)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderDetailResponse getById(UUID id) {
        OrderDetail orderDetail = orderDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail", id));
        return toResponseWithEarTag(orderDetail);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "ORDER_DETAIL")
    public OrderDetailResponse update(UUID id, UpdateOrderDetailRequest request, UUID updatedBy) {
        OrderDetail orderDetail = orderDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail", id));

        AuditContext.snapshot(orderDetail);
        orderDetailMapper.updateEntityFromRequest(request, orderDetail);
        orderDetail.setUpdatedBy(updatedBy);

        OrderDetail saved = orderDetailRepository.save(orderDetail);
        AuditContext.registerUpdated(saved);
        return toResponseWithEarTag(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "ORDER_DETAIL")
    public void delete(UUID id, UUID deletedBy) {
        OrderDetail orderDetail = orderDetailRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail", id));

        AuditContext.registerDeleted(orderDetail);
        orderDetail.setIsDeleted(true);
        orderDetail.setDeletedAt(Instant.now());
        orderDetail.setDeletedBy(deletedBy);
        orderDetailRepository.save(orderDetail);
    }

    private OrderDetailResponse toResponseWithEarTag(OrderDetail orderDetail) {
        OrderDetailResponse response = orderDetailMapper.toResponse(orderDetail);
        if (orderDetail.getPigId() != null) {
            pigRepository.findActiveById(orderDetail.getPigId())
                    .ifPresent(pig -> response.setPigEarTag(pig.getEarTag()));
        }
        return response;
    }
}
