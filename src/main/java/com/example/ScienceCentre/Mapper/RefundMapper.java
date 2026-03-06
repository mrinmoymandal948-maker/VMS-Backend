package com.example.ScienceCentre.Mapper;

import com.example.ScienceCentre.DTO.ResponseDto.BookingResponseDto;
import com.example.ScienceCentre.Model.Refund;
import java.math.BigDecimal;

public class RefundMapper {

    public static BookingResponseDto.TicketItemDto toRefundItem(Refund refund)
    {
        return new BookingResponseDto.TicketItemDto(
                null,
                null,
                1,
                refund.getRefundAmount() != null ? refund.getRefundAmount() : BigDecimal.ZERO,
                "REFUND"
        );
    }
}