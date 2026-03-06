package com.example.ScienceCentre.Mapper;

import com.example.ScienceCentre.DTO.ResponseDto.TicketResponseDto;
import com.example.ScienceCentre.Model.Payment;

public class PaymentMapper {
    public static TicketResponseDto toTicketResponse(Payment payment) {
        return new TicketResponseDto(
                payment.getTicket().getTicketNumber(),
                payment.getTicket().getVisitorName(),
                payment.getTicket().getStatus().name());
    }
}