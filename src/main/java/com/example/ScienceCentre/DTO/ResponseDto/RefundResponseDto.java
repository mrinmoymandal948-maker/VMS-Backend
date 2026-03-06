package com.example.ScienceCentre.DTO.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class RefundResponseDto
{
    private Long id;
    private String ticketNumber;
    private List<String> refundedTicketTypes;
    private BigDecimal refundAmount;
    private String reason;
    private String status;
}
