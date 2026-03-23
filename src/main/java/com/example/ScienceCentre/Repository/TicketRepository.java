package com.example.ScienceCentre.Repository;

import com.example.ScienceCentre.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long>
{
    Optional<Ticket> findByTicketNumber(String ticketNumber);

    @Query("""
   SELECT DISTINCT t
   FROM Ticket t
   LEFT JOIN FETCH t.ticketItems
   WHERE t.centre.id = :centreId
   AND DATE(t.bookingTime) = :date
   AND t.status <> 'CREATED'
""")
    List<Ticket> findByCentreIdAndBookingDate(Long centreId, LocalDate date);

    @Query("""
       SELECT DISTINCT t FROM Ticket t
       LEFT JOIN FETCH t.ticketItems
       WHERE t.centre.id = :centreId
       AND DATE(t.bookingTime) BETWEEN :start AND :end
       AND t.status <> 'CREATED'
""")
    List<Ticket> findByCentreIdAndBookingDateRange(
            @Param("centreId") Long centreId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end
    );

    @Query("SELECT COUNT(t) FROM Ticket t WHERE DATE(t.bookingTime) = :date")
    Long countTicketsByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(t) FROM Ticket t WHERE DATE(t.bookingTime) BETWEEN :start AND :end")
    Long countTicketsBetween(@Param("start") LocalDate start,
                             @Param("end") LocalDate end);

}
