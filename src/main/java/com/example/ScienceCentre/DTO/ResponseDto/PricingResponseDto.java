package com.example.ScienceCentre.DTO.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingResponseDto
{
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
}

