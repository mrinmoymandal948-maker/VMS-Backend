package com.example.ScienceCentre.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesReportDto {

    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalTickets;
    private Double totalRevenue;
    private Double totalRefund;
    private Double netRevenue;
}

