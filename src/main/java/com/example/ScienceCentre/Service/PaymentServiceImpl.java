package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.RequestDto.PaymentRequestDto;
import com.example.ScienceCentre.DTO.ResponseDto.TicketResponseDto;
import com.example.ScienceCentre.Enums.TicketStatus;
import com.example.ScienceCentre.Model.Payment;
import com.example.ScienceCentre.Model.Ticket;
import com.example.ScienceCentre.Repository.PaymentRepository;
import com.example.ScienceCentre.Repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Override
    @Transactional
    public TicketResponseDto confirmPayment(PaymentRequestDto request) {
        // 1. Find the ticket by number
        Ticket ticket = ticketRepository.findByTicketNumber(request.getTicketNumber())
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + request.getTicketNumber()));

        // 2. Map DTO to Payment Entity and save
        Payment payment = new Payment();
        payment.setTicket(ticket);
        payment.setPaymentMode(request.getPaymentMode());
        payment.setAmountPaid(request.getAmountPaid());
        payment.setReferenceNumber(request.getReferenceNumber());
        payment.setPaymentTime(LocalDateTime.now());
        paymentRepository.save(payment);

        // 3. Update Ticket status to CHECKED_IN (or PAID depending on your flow)
        ticket.setStatus(TicketStatus.PAID);
        Ticket updatedTicket = ticketRepository.save(ticket);

        // 4. Return the Response DTO
        TicketResponseDto response = new TicketResponseDto();
        response.setTicketNumber(updatedTicket.getTicketNumber());
        response.setVisitorName(updatedTicket.getVisitorName());
        response.setStatus(updatedTicket.getStatus().name());

        return response;
    }

    @Override
    public Object getPaymentByTicket(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        return paymentRepository.findByTicket(ticket)
                .map(Payment::getAmountPaid)
                .orElse(java.math.BigDecimal.ZERO);
    }
}
