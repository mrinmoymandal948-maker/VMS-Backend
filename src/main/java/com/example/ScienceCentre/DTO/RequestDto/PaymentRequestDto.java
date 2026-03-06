package com.example.ScienceCentre.DTO.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto
{
    private String ticketNumber;
    private String paymentMode;
    private String referenceNumber;
    private BigDecimal amountPaid;
}

