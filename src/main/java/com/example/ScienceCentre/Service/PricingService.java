package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.RequestDto.PricingQueryDto;
import com.example.ScienceCentre.DTO.ResponseDto.PricingResponseDto;

public interface PricingService
{
    PricingResponseDto calculatePrice(PricingQueryDto pricingQuery);
}
