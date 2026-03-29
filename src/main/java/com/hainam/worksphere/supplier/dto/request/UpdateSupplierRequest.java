package com.hainam.worksphere.supplier.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSupplierRequest {

    @Size(max = 150)
    private String name;

    private String address;

    @Size(max = 20)
    private String phone;

    @Size(max = 100)
    private String email;
}
