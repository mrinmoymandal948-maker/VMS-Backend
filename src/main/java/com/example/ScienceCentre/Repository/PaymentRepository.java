package com.example.ScienceCentre.Repository;

import com.example.ScienceCentre.Model.Payment;
import com.example.ScienceCentre.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>
{
    Optional<Payment> findByTicket(Ticket ticket);

    @Query("SELECT COALESCE(SUM(p.amountPaid),0) FROM Payment p WHERE DATE(p.paymentTime) = :date")
    Double sumRevenueByDate(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(p.amountPaid),0) FROM Payment p WHERE DATE(p.paymentTime) BETWEEN :start AND :end")
    Double sumRevenueBetween(@Param("start") LocalDate start,
                             @Param("end") LocalDate end);

}

