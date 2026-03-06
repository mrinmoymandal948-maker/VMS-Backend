package com.example.ScienceCentre.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket;

    @Column(nullable = false)
    private String paymentMode;

    @Column(precision = 10, scale = 2, nullable = false)
    private BigDecimal amountPaid;

    @Column
    private String referenceNumber;

    @Column(nullable = false)
    private LocalDateTime paymentTime;

    @Column(name = "payment_mode_id")
    private Long paymentModeId;

}

