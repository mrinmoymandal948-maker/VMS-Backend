package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.RequestDto.BookingRequestDto;
import com.example.ScienceCentre.DTO.ResponseDto.BookingResponseDto;
import com.example.ScienceCentre.Enums.TicketStatus;
import com.example.ScienceCentre.Model.*;
import com.example.ScienceCentre.Repository.CentreRepository;
import com.example.ScienceCentre.Repository.LookupListRepository;
import com.example.ScienceCentre.Repository.PricingRepository;
import com.example.ScienceCentre.Repository.TicketRepository;
import com.example.ScienceCentre.Util.TicketNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    @Autowired
    private LookupListRepository lookupListRepository;

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

        List<LookupList> ticketTypes = lookupListRepository.findByLookupCode("TICKET_TYPE");

        if (ticketTypes.isEmpty()) {
            throw new RuntimeException("No ticket types configured in lookup");
        }

        String entryTicketValue = ticketTypes.get(0).getLookupValue();

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

            item.setSlotTime(entryTicketValue.equals(ticketTypeValue) ? "Full Day" : "Scheduled");

            items.add(item);
        }

        ticket.setTicketItems(items);
        Ticket savedTicket = ticketRepository.save(ticket);

        return mapToResponse(savedTicket);
    }

    @Override
    public List<BookingResponseDto> getTodayTickets(Long centreId) {

        LocalDate today = LocalDate.now();

        List<Ticket> tickets = ticketRepository
                .findByCentreIdAndBookingDate(centreId, today);

        return tickets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private BookingResponseDto mapToResponse(Ticket ticket) {
        BookingResponseDto response = new BookingResponseDto();
        response.setId(ticket.getId());
        response.setTicketNumber(ticket.getTicketNumber());
        response.setBookingTime(ticket.getBookingTime());

        response.setStatus(ticket.getStatus().name());

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

    @Override
    public List<BookingResponseDto> getTicketsByDate(Long centreId, LocalDate date) {
        List<Ticket> tickets = ticketRepository.findByCentreIdAndBookingDate(centreId, date);
        return tickets.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Override
    public List<BookingResponseDto> getTicketsByMonth(Long centreId, int month, int year) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return getTicketsByRange(centreId, start, end);
    }

    @Override
    public List<BookingResponseDto> getTicketsByYear(Long centreId, int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        return getTicketsByRange(centreId, start, end);
    }

    @Override
    public List<BookingResponseDto> getTicketsByRange(Long centreId, LocalDate start, LocalDate end) {
        // FIX: Call the repository method that uses JOIN FETCH
        List<Ticket> tickets = ticketRepository.findByCentreIdAndBookingDateRange(centreId, start, end);

        return tickets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}