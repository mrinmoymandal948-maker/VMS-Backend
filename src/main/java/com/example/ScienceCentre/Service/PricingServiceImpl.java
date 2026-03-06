package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.RequestDto.PricingQueryDto;
import com.example.ScienceCentre.DTO.ResponseDto.PricingResponseDto;
import com.example.ScienceCentre.Exception.BusinessException;
import com.example.ScienceCentre.Model.Centre;
import com.example.ScienceCentre.Model.PricingConfig;
import com.example.ScienceCentre.Repository.CentreRepository;
import com.example.ScienceCentre.Repository.PricingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PricingServiceImpl implements PricingService {

    @Autowired
    private PricingRepository pricingRepository;

    @Autowired
    private CentreRepository centreRepository;

    @Override
    public PricingResponseDto calculatePrice(PricingQueryDto pricingQuery) {

        // 1. Validate centre
        Centre centre = centreRepository.findById(pricingQuery.getCentreId())
                .orElseThrow(() -> new BusinessException("Invalid location"));

        // LOOKUP-DRIVEN VALUES (NO ENUMS)
        String ticketTypeValue = pricingQuery.getTicketType();       // already String
        String visitorCategoryValue = pricingQuery.getVisitorCategory();

        // 2. Fetch pricing config
        PricingConfig pricing = pricingRepository
                .findByCentreAndTicketTypeAndVisitorCategoryAndActive(
                        centre,
                        ticketTypeValue,
                        visitorCategoryValue,
                        true
                )
                .orElseThrow(() ->
                        new BusinessException(
                                "Pricing not configured for TicketType="
                                        + ticketTypeValue
                                        + ", VisitorCategory="
                                        + visitorCategoryValue
                        )
                );

        // 3. Calculate total
        BigDecimal total = pricing.getPrice()
                .multiply(BigDecimal.valueOf(pricingQuery.getQuantity()));

        return new PricingResponseDto(
                pricing.getPrice(),
                total
        );
    }
}
