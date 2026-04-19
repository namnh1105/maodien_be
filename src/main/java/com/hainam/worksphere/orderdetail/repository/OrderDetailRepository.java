package com.hainam.worksphere.orderdetail.repository;

import com.hainam.worksphere.orderdetail.domain.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

    @Query("SELECT od FROM OrderDetail od WHERE od.isDeleted = false")
    List<OrderDetail> findAllActive();

    @Query("SELECT od FROM OrderDetail od WHERE od.id = :id AND od.isDeleted = false")
    Optional<OrderDetail> findActiveById(@Param("id") UUID id);
}
