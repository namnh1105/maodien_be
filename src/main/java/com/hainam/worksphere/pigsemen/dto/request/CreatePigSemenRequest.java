package com.hainam.worksphere.pigsemen.dto.request;

import jakarta.validation.constraints.NotBlank;
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
public class CreatePigSemenRequest {

    @NotBlank(message = "Mã tinh không được để trống")
    private String code;
    private UUID boarPigId;
    private String boarBreed;
    private LocalDate collectionDate;
    private Double volume;
    private Double motility;
    private String quality;
    private String status;
    private String note;
}
