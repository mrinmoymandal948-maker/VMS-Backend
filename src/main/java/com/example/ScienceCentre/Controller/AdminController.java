package com.example.ScienceCentre.Controller;

import com.example.ScienceCentre.Service.DashboardService;
import com.example.ScienceCentre.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/admin")
public class AdminController
{
    @Autowired
    private ReportService reportService;

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard() {
        return ResponseEntity.ok(dashboardService.getTodaySummary());
    }

    @GetMapping("/reports/daily")
    public ResponseEntity<?> getDailyReport(@RequestParam String date) {
        return ResponseEntity.ok(
                reportService.getDailyReport(LocalDate.parse(date))
        );
    }

    @GetMapping("/reports/monthly")
    public ResponseEntity<?> getMonthlyReport(@RequestParam int month,
                                              @RequestParam int year) {
        return ResponseEntity.ok(
                reportService.getMonthlyReport(month, year)
        );
    }

    @GetMapping("/reports/yearly")
    public ResponseEntity<?> getYearlyReport(@RequestParam int year) {
        return ResponseEntity.ok(
                reportService.getYearlyReport(year)
        );
    }

    @GetMapping("/reports/custom")
    public ResponseEntity<?> getCustomReport(@RequestParam String start,
                                             @RequestParam String end) {
        return ResponseEntity.ok(
                reportService.getCustomReport(
                        LocalDate.parse(start),
                        LocalDate.parse(end)
                )
        );
    }
}
