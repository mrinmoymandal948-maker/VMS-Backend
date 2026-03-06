package com.example.ScienceCentre.DTO.RequestDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class BookingRequestDto {

    @NotBlank
    private String visitorName;

    @NotBlank
    private String phoneNumber;

    @NotNull
    private Long centreId;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    private List<TicketItemRequestDto> items;

    @Data
    public static class TicketItemRequestDto {
        private String ticketType;   // holds lookup_value
        private Long ticketTypeId;
        private String visitorCategory; // lookup_value
        private Integer quantity;
    }
}
