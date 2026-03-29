package com.hainam.worksphere.supplier.dto.response;

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
public class SupplierResponse {

    private UUID id;
    private String supplierCode;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Instant createdAt;
    private Instant updatedAt;
}
