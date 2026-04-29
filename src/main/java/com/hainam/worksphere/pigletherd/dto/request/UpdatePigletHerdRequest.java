package com.hainam.worksphere.pigletherd.dto.request;

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
public class UpdatePigletHerdRequest {
    private Integer litterNumber;

    private UUID motherId;

    private UUID fatherId;

    private Integer quantity;

    @Size(max = 100)
    private String genderNote;

    private Double averageBirthWeight;

    private LocalDate birthDate;

    private UUID semenId;

    private String status;
}
