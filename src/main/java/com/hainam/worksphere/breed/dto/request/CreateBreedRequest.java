package com.hainam.worksphere.breed.dto.request;

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
public class CreateBreedRequest {

    @NotBlank(message = "Mã giống không được để trống")
    @NotBlank(message = "Tên giống không được để trống")
    @Size(max = 150)
    private String name;

    private String characteristics;
}
