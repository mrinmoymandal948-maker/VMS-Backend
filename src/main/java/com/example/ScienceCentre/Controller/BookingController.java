package com.example.ScienceCentre.Controller;

import com.example.ScienceCentre.DTO.RequestDto.BookingRequestDto;
import com.example.ScienceCentre.DTO.ResponseDto.BookingResponseDto;
import com.example.ScienceCentre.Repository.TicketRepository;
import com.example.ScienceCentre.Service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/today/{centreId}")
    public ResponseEntity<?> getTodayTickets(@PathVariable Long centreId) {
        return ResponseEntity.ok(bookingService.getTodayTickets(centreId));
    }

    @GetMapping("/filter/date")
    public ResponseEntity<?> getTicketsByDate(
            @RequestParam Long centreId,
            @RequestParam String date
    ) {
        return ResponseEntity.ok(
                bookingService.getTicketsByDate(centreId, LocalDate.parse(date))
        );
    }

    @GetMapping("/filter/month")
    public ResponseEntity<?> getTicketsByMonth(
            @RequestParam Long centreId,
            @RequestParam int month,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                bookingService.getTicketsByMonth(centreId, month, year)
        );
    }

    @GetMapping("/filter/year")
    public ResponseEntity<?> getTicketsByYear(
            @RequestParam Long centreId,
            @RequestParam int year
    ) {
        return ResponseEntity.ok(
                bookingService.getTicketsByYear(centreId, year)
        );
    }

    @GetMapping("/filter/range")
    public ResponseEntity<?> getTicketsByRange(
            @RequestParam Long centreId,
            @RequestParam String start,
            @RequestParam String end
    ) {
        return ResponseEntity.ok(
                bookingService.getTicketsByRange(centreId, LocalDate.parse(start), LocalDate.parse(end))
        );
    }
}
