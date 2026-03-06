package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.SalesReportDto;

import java.time.LocalDate;

public interface ReportService
{
    SalesReportDto getDailyReport(LocalDate date);
    SalesReportDto getMonthlyReport(int month, int year);
    SalesReportDto getYearlyReport(int year);
    SalesReportDto getCustomReport(LocalDate start, LocalDate end);
}

