package com.example.ScienceCentre.Mapper;

import com.example.ScienceCentre.DTO.ResponseDto.BookingResponseDto;
import com.example.ScienceCentre.DTO.ResponseDto.TicketResponseDto;
import com.example.ScienceCentre.Model.Ticket;

import java.util.stream.Collectors;

public class TicketMapper {

    public static BookingResponseDto toBookingResponse(Ticket ticket) {

        return new BookingResponseDto(
                ticket.getId(),
                ticket.getTicketNumber(),
                ticket.getBookingTime(),
                ticket.getTicketItems()
                        .stream()
                        .map(item -> new BookingResponseDto.TicketItemDto(
                                item.getTicketType(),
                                // Get the category from the parent ticket
                                ticket.getVisitorCategory(),
                                item.getQuantity(),
                                item.getAmount(),
                                item.getSlotTime()
                        ))
                        .collect(Collectors.toList())
        );
    }

    public static TicketResponseDto toTicketResponse(Ticket ticket) {
        return new TicketResponseDto(
                ticket.getTicketNumber(),
                ticket.getVisitorName(),
                ticket.getStatus().name()
        );
    }
}