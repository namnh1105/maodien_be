package com.hainam.worksphere.pig.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePigRequest {

    @NotBlank(message = "Mã lợn không được để trống")
    @Size(max = 30)
    private String pigCode;

    @Size(max = 50)
    private String earTag;

    private Double birthWeight;

    private LocalDate birthDate;

    @NotNull(message = "Giới tính không được để trống")
    private String gender;

    @Size(max = 100)
    private String species;

    private Integer nippleCount;

    private LocalDate herdEntryDate;

    private String status;
}
