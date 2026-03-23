package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.RequestDto.BookingRequestDto;
import com.example.ScienceCentre.DTO.ResponseDto.BookingResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto bookingRequest);
    List<BookingResponseDto> getTodayTickets(Long centreId);

    List<BookingResponseDto> getTicketsByDate(Long centreId, LocalDate date);
    List<BookingResponseDto> getTicketsByMonth(Long centreId, int month, int year);
    List<BookingResponseDto> getTicketsByYear(Long centreId, int year);
    List<BookingResponseDto> getTicketsByRange(Long centreId, LocalDate start, LocalDate end);
}
