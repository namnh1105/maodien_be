package com.hainam.worksphere.customer.repository;

import com.hainam.worksphere.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query("SELECT c FROM Customer c WHERE c.isDeleted = false")
    List<Customer> findAllActive();

    @Query("SELECT c FROM Customer c WHERE c.id = :id AND c.isDeleted = false")
    Optional<Customer> findActiveById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c WHERE c.customerCode = :code AND c.isDeleted = false")
    boolean existsActiveByCustomerCode(@Param("code") String code);
}
