package com.example.ScienceCentre.DTO.ResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDto
{
    private String ticketNumber;
    private String visitorName;
    private String status;
}

