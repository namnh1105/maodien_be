package com.hainam.worksphere.customer.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCustomerRequest {

    @NotBlank(message = "Customer code is required")
    @NotBlank(message = "Customer name is required")
    @Size(max = 150)
    private String name;

    private String address;

    @Size(max = 20)
    private String phone;

    @Size(max = 100)
    private String email;

    private String customerType;
}
