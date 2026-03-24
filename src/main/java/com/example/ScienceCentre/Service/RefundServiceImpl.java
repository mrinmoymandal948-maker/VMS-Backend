package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.RequestDto.RefundRequestDto;
import com.example.ScienceCentre.DTO.ResponseDto.RefundResponseDto;
import com.example.ScienceCentre.Enums.RefundStatus;
import com.example.ScienceCentre.Enums.TicketStatus;
import com.example.ScienceCentre.Exception.BusinessException;
import com.example.ScienceCentre.Model.*;
import com.example.ScienceCentre.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RefundServiceImpl implements RefundService {

    @Autowired
    private RefundRepository refundRepository;

    @Autowired
    private LookupListRepository lookupListRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ApplicationConfigRepository applicationConfigRepository;


    @Override
    @Transactional
    public Map<String, Object> getRefundableTicketDetails(String ticketNumber) {

        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber)
                .orElseThrow(() -> new BusinessException("Invalid ticket number"));

        // Get all APPROVED or PENDING refunds for this ticket
        List<Refund> refunds = refundRepository
                .findByPaymentTicketTicketNumber(ticketNumber);

        // Sum up total refunded amount from APPROVED + PENDING refunds
        BigDecimal totalRefunded = refunds.stream()
                .filter(r -> r.getStatus() == RefundStatus.APPROVED
                        || r.getStatus() == RefundStatus.PENDING)
                .map(Refund::getRefundAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Match refunded amount against ticket items to find which types were refunded
        // We greedily match items until the refunded amount is accounted for
        List<TicketItem> allItems = ticket.getTicketItems();
        BigDecimal remaining = totalRefunded;
        List<String> refundedTypes = new ArrayList<>();

        for (TicketItem item : allItems) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) break;
            if (item.getAmount().compareTo(BigDecimal.ZERO) > 0
                    && remaining.compareTo(item.getAmount()) >= 0) {
                refundedTypes.add(item.getTicketType());
                remaining = remaining.subtract(item.getAmount());
            }
        }

        // Build items list with quantity = 0 if already refunded
        List<Map<String, Object>> items = allItems.stream()
                .map(item -> {
                    Map<String, Object> itemMap = new HashMap<>();
                    itemMap.put("ticketType", item.getTicketType());
                    itemMap.put("amount", item.getAmount());
                    itemMap.put("quantity", refundedTypes.contains(item.getTicketType()) ? 0 : item.getQuantity());
                    return itemMap;
                })
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("ticketNumber", ticket.getTicketNumber());
        response.put("status", ticket.getStatus().name());
        response.put("items", items);

        return response;
    }


    @Override
    @Transactional
    public void processRefund(RefundRequestDto request) {

        Ticket ticket = ticketRepository.findByTicketNumber(request.getTicketNumber())
                .orElseThrow(() -> new BusinessException("Ticket not found"));

        // Check refund time limit
        LocalDateTime bookingTime = ticket.getBookingTime();

        if (!bookingTime.toLocalDate().equals(LocalDateTime.now().toLocalDate())) {
            throw new BusinessException("Refund time is expired");
        }

        Payment payment = paymentRepository.findByTicket(ticket)
                .orElseThrow(() -> new BusinessException("Payment not found"));

        List<TicketItem> items = ticket.getTicketItems();

        BigDecimal refundAmount = BigDecimal.ZERO;

        for (String type : request.getTicketTypes()) {

            TicketItem item = items.stream()
                    .filter(i -> i.getTicketType().equals(type))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException("Invalid ticket type"));

            refundAmount = refundAmount.add(item.getAmount());
        }

        if (refundAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Nothing to refund");
        }

        Refund refund = new Refund();
        refund.setPayment(payment);
        refund.setRefundAmount(refundAmount);
        refund.setReason(request.getReason());
        refund.setRequestedAt(LocalDateTime.now());

        Long centreId = ticket.getCentre().getId();
        Optional<ApplicationConfig> refundConfigOpt = applicationConfigRepository
                .findByCentreIdAndConfigKeyAndActiveTrue(centreId, "REFUND_APPROVAL_REQUIRED");

        boolean approvalRequired = refundConfigOpt
                .map(c -> Boolean.parseBoolean(c.getConfigValue()))
                .orElse(true);

        refund.setStatus(approvalRequired ? RefundStatus.PENDING : RefundStatus.APPROVED);

        refundRepository.save(refund);

        if (!approvalRequired) {

            List<String> requestedTypes = request.getTicketTypes();

            List<LookupList> ticketTypes = lookupListRepository.findByLookupCode("TICKET_TYPE");

            if (ticketTypes.isEmpty()) {
                throw new BusinessException("Ticket types not configured");
            }

            String entryTicketValue = ticketTypes.get(0).getLookupValue();

            boolean entryRefunded = requestedTypes.contains(entryTicketValue);

            if (entryRefunded) {
                ticket.setStatus(TicketStatus.FULLY_REFUNDED);
            } else {
                ticket.setStatus(TicketStatus.PARTIALLY_REFUNDED);
            }

            ticketRepository.save(ticket);
        }
    }


    @Override
    @Transactional
    public void updateRefundStatus(Long id, RefundStatus status, String reason) {

        Refund refund = refundRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Refund not found"));

        if (refund.getStatus() != RefundStatus.PENDING) {
            throw new BusinessException("Already processed");
        }

        refund.setStatus(status);

        if (status == RefundStatus.REJECTED && reason != null && !reason.isBlank()) {
            refund.setReason(refund.getReason() + " | REJECTED: " + reason);
        }

        refundRepository.save(refund);

        if (status == RefundStatus.APPROVED) {

            Ticket ticket = refund.getPayment().getTicket();

            List<LookupList> ticketTypes = lookupListRepository.findByLookupCode("TICKET_TYPE");

            if (ticketTypes.isEmpty()) {
                throw new BusinessException("Ticket types not configured");
            }

            String entryTicketValue = ticketTypes.get(0).getLookupValue();

            boolean entryExists = ticket.getTicketItems()
                    .stream()
                    .anyMatch(i -> i.getTicketType().equals(entryTicketValue));

            if (entryExists) {
                ticket.setStatus(TicketStatus.FULLY_REFUNDED);
            } else {
                ticket.setStatus(TicketStatus.PARTIALLY_REFUNDED);
            }

            ticketRepository.save(ticket);
        }
    }

    @Override
    public RefundResponseDto getLatestRefund(String ticketNumber) {

        Refund refund = refundRepository
                .findTopByPaymentTicketTicketNumberOrderByRequestedAtDesc(ticketNumber)
                .orElse(null);

        if (refund == null) return null;

        return new RefundResponseDto(
                refund.getId(),
                ticketNumber,
                Collections.emptyList(),
                refund.getRefundAmount(),
                refund.getReason(),
                refund.getStatus().name()
        );
    }


    @Override
    @Transactional
    public List<RefundResponseDto> getPendingRefunds() {

        List<Refund> refunds = refundRepository.findByStatus(RefundStatus.PENDING);

        if (refunds.isEmpty()) {
            return Collections.emptyList();
        }

        return refunds.stream()
                .map(r -> {
                    String ticketNumber = null;

                    if (r.getPayment() != null && r.getPayment().getTicket() != null) {
                        ticketNumber = r.getPayment().getTicket().getTicketNumber();
                    }

                    return new RefundResponseDto(
                            r.getId(),
                            ticketNumber,
                            Collections.emptyList(),
                            r.getRefundAmount(),
                            r.getReason(),
                            r.getStatus().name()
                    );
                })
                .toList();
    }
}
