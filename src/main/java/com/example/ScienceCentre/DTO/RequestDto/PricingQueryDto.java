package com.example.ScienceCentre.DTO.RequestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingQueryDto {

    @NotNull
    private String ticketType;

    @NotNull
    private String visitorCategory;

    @NotNull
    private Long centreId;

    @Min(1)
    private int quantity;
}
