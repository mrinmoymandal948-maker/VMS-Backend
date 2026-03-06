package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.RequestDto.BookingRequestDto;
import com.example.ScienceCentre.DTO.ResponseDto.BookingResponseDto;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto bookingRequest);
}
