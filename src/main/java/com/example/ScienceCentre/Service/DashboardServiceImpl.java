package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.DashboardSummaryDto;
import com.example.ScienceCentre.Enums.RefundStatus;
import com.example.ScienceCentre.Repository.PaymentRepository;
import com.example.ScienceCentre.Repository.RefundRepository;
import com.example.ScienceCentre.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class DashboardServiceImpl implements DashboardService
{
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final RefundRepository refundRepository;

    public DashboardServiceImpl(TicketRepository ticketRepository,
                                PaymentRepository paymentRepository,
                                RefundRepository refundRepository) {
        this.ticketRepository = ticketRepository;
        this.paymentRepository = paymentRepository;
        this.refundRepository = refundRepository;
    }

    @Override
    public DashboardSummaryDto getTodaySummary() {

        LocalDate today = LocalDate.now();

        Long tickets = ticketRepository.countTicketsByDate(today);
        Double revenue = paymentRepository.sumRevenueByDate(today);
        Double refund = refundRepository.sumApprovedRefundByDate(today);
        Long pending = refundRepository.countByStatus(RefundStatus.PENDING);

        tickets = (tickets == null) ? 0L : tickets;
        revenue = (revenue == null) ? 0.0 : revenue;
        refund = (refund == null) ? 0.0 : refund;
        pending = (pending == null) ? 0L : pending;

        Double net = revenue - refund;

        return new DashboardSummaryDto(
                tickets,
                revenue,
                refund,
                net,
                pending
        );
    }

}
