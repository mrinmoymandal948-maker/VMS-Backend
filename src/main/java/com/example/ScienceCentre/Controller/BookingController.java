package com.example.ScienceCentre.Controller;

import com.example.ScienceCentre.DTO.RequestDto.BookingRequestDto;
import com.example.ScienceCentre.DTO.ResponseDto.BookingResponseDto;
import com.example.ScienceCentre.Repository.TicketRepository;
import com.example.ScienceCentre.Service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/bookings")
public class BookingController
{

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TicketRepository ticketRepository;

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto bookingRequest)
    {
        return ResponseEntity.ok(bookingService.createBooking(bookingRequest));
    }
}
