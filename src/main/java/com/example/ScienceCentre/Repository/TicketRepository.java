package com.example.ScienceCentre.Repository;

import com.example.ScienceCentre.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>
{
    Optional<Ticket> findByTicketNumber(String ticketNumber);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE DATE(t.bookingTime) = :date")
    Long countTicketsByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE DATE(t.bookingTime) BETWEEN :start AND :end")
    Long countTicketsBetween(@Param("start") LocalDate start,
                             @Param("end") LocalDate end);

}
