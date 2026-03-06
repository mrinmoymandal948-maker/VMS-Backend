package com.example.ScienceCentre.Controller;

import com.example.ScienceCentre.DTO.RequestDto.PricingQueryDto;
import com.example.ScienceCentre.DTO.ResponseDto.PricingResponseDto;
import com.example.ScienceCentre.Service.PricingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pricing")
public class PricingController {

    @Autowired
    private PricingService pricingService;

    @PostMapping("/calculate")
    public ResponseEntity<PricingResponseDto> calculatePrice(@Valid @RequestBody PricingQueryDto pricingQuery)
    {
        return ResponseEntity.ok(pricingService.calculatePrice(pricingQuery));
    }
}
