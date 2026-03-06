package com.example.ScienceCentre.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardSummaryDto {

    private Long totalTickets;
    private Double totalRevenue;
    private Double totalRefund;
    private Double netRevenue;
    private Long pendingRefunds;
}
