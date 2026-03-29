package com.hainam.worksphere.pigletherd.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePigletHerdRequest {

    @NotBlank(message = "Herd code is required")
    @Size(max = 30)
    private String herdCode;

    private UUID motherId;

    private UUID fatherId;

    private Integer quantity;

    @Size(max = 100)
    private String genderNote;

    private Double averageBirthWeight;

    private LocalDate birthDate;
}
