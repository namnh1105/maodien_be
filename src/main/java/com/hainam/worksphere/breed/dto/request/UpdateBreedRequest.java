package com.hainam.worksphere.breed.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBreedRequest {

    @Size(max = 150)
    private String name;

    @Size(max = 50)
    private String code;

    private String characteristics;
}
