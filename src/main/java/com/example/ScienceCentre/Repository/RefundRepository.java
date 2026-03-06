package com.example.ScienceCentre.Repository;

import com.example.ScienceCentre.Enums.RefundStatus;
import com.example.ScienceCentre.Model.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long>
{
    List<Refund> findByStatus(RefundStatus status);
    // Used by the Frontend to check if the status changed for a ticket
    Optional<Refund> findTopByPaymentTicketTicketNumberOrderByRequestedAtDesc(String ticketNumber);
    @Query("SELECT COUNT(r) FROM Refund r WHERE r.status='PENDING'")
    Long countByStatus(RefundStatus status);

    @Query("SELECT COALESCE(SUM(r.refundAmount),0) FROM Refund r WHERE r.status = com.example.ScienceCentre.Enums.RefundStatus.APPROVED AND DATE(r.requestedAt) = :date")
    Double sumApprovedRefundByDate(@Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(r.refundAmount),0) FROM Refund r WHERE r.status = com.example.ScienceCentre.Enums.RefundStatus.APPROVED AND DATE(r.requestedAt) BETWEEN :start AND :end")
    Double sumApprovedRefundBetween(@Param("start") LocalDate start,
                                    @Param("end") LocalDate end);
}
