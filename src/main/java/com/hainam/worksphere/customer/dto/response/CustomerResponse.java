package com.hainam.worksphere.customer.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private UUID id;
    private String customerCode;
    private String name;
    private String address;
    private String phone;
    private String email;
    private String customerType;
    private Instant createdAt;
    private Instant updatedAt;
}
