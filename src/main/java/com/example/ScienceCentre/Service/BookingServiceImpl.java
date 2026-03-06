package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.RequestDto.BookingRequestDto;
import com.example.ScienceCentre.DTO.ResponseDto.BookingResponseDto;
import com.example.ScienceCentre.Enums.TicketStatus;
import com.example.ScienceCentre.Model.Centre;
import com.example.ScienceCentre.Model.PricingConfig;
import com.example.ScienceCentre.Model.Ticket;
import com.example.ScienceCentre.Model.TicketItem;
import com.example.ScienceCentre.Repository.CentreRepository;
import com.example.ScienceCentre.Repository.PricingRepository;
import com.example.ScienceCentre.Repository.TicketRepository;
import com.example.ScienceCentre.Util.TicketNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private PricingRepository pricingRepository;

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto request) {

        Centre centre = centreRepository.findById(request.getCentreId())
                .orElseThrow(() -> new RuntimeException("Centre not found"));

        Ticket ticket = new Ticket();
        ticket.setVisitorName(request.getVisitorName());
        ticket.setPhoneNumber(request.getPhoneNumber());
        ticket.setCentre(centre);
        ticket.setBookingTime(LocalDateTime.now());
        ticket.setStatus(TicketStatus.CREATED);
        ticket.setTicketNumber(TicketNumberGenerator.generate());

        // Set visitor category on Ticket from the first item in the request
        if (request.getItems() != null && !request.getItems().isEmpty()) {
            ticket.setVisitorCategory(request.getItems().get(0).getVisitorCategory());
        }

        List<TicketItem> items = new ArrayList<>();

        for (BookingRequestDto.TicketItemRequestDto itemDto : request.getItems()) {

            String ticketTypeValue = itemDto.getTicketType();
            // Use the category stored in the Ticket entity
            String visitorCategoryValue = ticket.getVisitorCategory();

            PricingConfig config = pricingRepository
                    .findByCentreAndTicketTypeAndVisitorCategoryAndActive(
                            centre,
                            ticketTypeValue,
                            visitorCategoryValue,
                            true
                    )
                    .orElseThrow(() -> new RuntimeException(
                            "Pricing not found for TicketType=" + ticketTypeValue
                                    + ", VisitorCategory=" + visitorCategoryValue
                    ));

            TicketItem item = new TicketItem();
            item.setTicket(ticket);
            item.setTicketType(ticketTypeValue);
            // item.setVisitorCategory is removed because the field is now in Ticket
            item.setQuantity(itemDto.getQuantity());
            item.setAmount(config.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));

            item.setSlotTime("GENERAL_ENTRY".equals(ticketTypeValue) ? "Full Day" : "Scheduled");

            items.add(item);
        }

        ticket.setTicketItems(items);
        Ticket savedTicket = ticketRepository.save(ticket);

        return mapToResponse(savedTicket);
    }

    private BookingResponseDto mapToResponse(Ticket ticket) {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(ticket.getId());
        response.setTicketNumber(ticket.getTicketNumber());
        response.setBookingTime(ticket.getBookingTime());

        response.setItems(
                ticket.getTicketItems().stream().map(i -> {
                    BookingResponseDto.TicketItemDto d = new BookingResponseDto.TicketItemDto();
                    d.setTicketType(i.getTicketType());
                    // Pull from the parent Ticket
                    d.setVisitorCategory(ticket.getVisitorCategory());
                    d.setQuantity(i.getQuantity());
                    d.setAmount(i.getAmount());
                    d.setSlotTime(i.getSlotTime());
                    return d;
                }).collect(Collectors.toList())
        );

        return response;
    }
}