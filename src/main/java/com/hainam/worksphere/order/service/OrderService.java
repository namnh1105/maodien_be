package com.hainam.worksphere.order.service;

import com.hainam.worksphere.order.domain.Order;
import com.hainam.worksphere.order.dto.request.CreateOrderRequest;
import com.hainam.worksphere.order.dto.request.UpdateOrderRequest;
import com.hainam.worksphere.order.dto.response.OrderResponse;
import com.hainam.worksphere.order.mapper.OrderMapper;
import com.hainam.worksphere.order.repository.OrderRepository;
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
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    @AuditAction(type = ActionType.CREATE, entity = "ORDER")
    public OrderResponse create(CreateOrderRequest request, UUID createdBy) {
        Order order = orderMapper.toEntity(request);
        order.setCreatedBy(createdBy);

        Order saved = orderRepository.save(order);
        AuditContext.registerCreated(saved);
        return orderMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAll() {
        return orderRepository.findAllActive().stream().map(orderMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getById(UUID id) {
        Order order = orderRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));
        return orderMapper.toResponse(order);
    }

    @Transactional
    @AuditAction(type = ActionType.UPDATE, entity = "ORDER")
    public OrderResponse update(UUID id, UpdateOrderRequest request, UUID updatedBy) {
        Order order = orderRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        AuditContext.snapshot(order);
        orderMapper.updateEntityFromRequest(request, order);
        order.setUpdatedBy(updatedBy);

        Order saved = orderRepository.save(order);
        AuditContext.registerUpdated(saved);
        return orderMapper.toResponse(saved);
    }

    @Transactional
    @AuditAction(type = ActionType.DELETE, entity = "ORDER")
    public void delete(UUID id, UUID deletedBy) {
        Order order = orderRepository.findActiveById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", id));

        AuditContext.registerDeleted(order);
        order.setIsDeleted(true);
        order.setDeletedAt(Instant.now());
        order.setDeletedBy(deletedBy);
        orderRepository.save(order);
    }
}
