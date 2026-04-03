package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.RequestDto.PaymentRequestDto;
import com.example.ScienceCentre.DTO.ResponseDto.TicketResponseDto;

public interface PaymentService
{
    TicketResponseDto confirmPayment(PaymentRequestDto request);
    Object getPaymentByTicket(String ticketNumber);
}
