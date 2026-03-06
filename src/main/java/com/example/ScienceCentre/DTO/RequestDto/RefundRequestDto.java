package com.example.ScienceCentre.DTO.RequestDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequestDto
{
    private String ticketNumber;
    private List<String> ticketTypes;
    private String reason;
}
