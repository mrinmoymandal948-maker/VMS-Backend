package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.SalesReportDto;
import com.example.ScienceCentre.Repository.PaymentRepository;
import com.example.ScienceCentre.Repository.RefundRepository;
import com.example.ScienceCentre.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private RefundRepository refundRepository;

    @Override
    public SalesReportDto getDailyReport(LocalDate date) {

        Long tickets = ticketRepository.countTicketsByDate(date);
        Double revenue = paymentRepository.sumRevenueByDate(date);
        Double refund = refundRepository.sumApprovedRefundByDate(date);

        Double net = revenue - refund;

        return new SalesReportDto(date, date, tickets, revenue, refund, net);
    }

    @Override
    public SalesReportDto getMonthlyReport(int month, int year) {

        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

        return getCustomReport(start, end);
    }

    @Override
    public SalesReportDto getYearlyReport(int year) {

        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        return getCustomReport(start, end);
    }

    @Override
    public SalesReportDto getCustomReport(LocalDate start, LocalDate end) {

        Long tickets = ticketRepository.countTicketsBetween(start, end);
        Double revenue = paymentRepository.sumRevenueBetween(start, end);
        Double refund = refundRepository.sumApprovedRefundBetween(start, end);

        Double net = revenue - refund;

        return new SalesReportDto(start, end, tickets, revenue, refund, net);
    }
}
