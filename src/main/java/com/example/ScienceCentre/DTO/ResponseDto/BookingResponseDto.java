package com.example.ScienceCentre.DTO.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto
{
    private Long id;
    private String ticketNumber;
    private LocalDateTime bookingTime;
    private String status;
    private List<TicketItemDto> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TicketItemDto {
        private String ticketType; // Changed to String to match your Service implementation
        private String visitorCategory; // Changed to String to match your Service implementation
        private int quantity;
        private BigDecimal amount;
        private String slotTime; // NEW: To send assigned show times back to the frontend
    }
}