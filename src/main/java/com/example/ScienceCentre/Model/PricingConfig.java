package com.example.ScienceCentre.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "pricing_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PricingConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Boolean active;

    private BigDecimal price;

    @Column(name = "ticket_type")
    private String ticketType;

    @Column(name = "ticket_type_id")
    private Long ticketTypeId;

    @Column(name = "visitor_category")
    private String visitorCategory;

    @ManyToOne
    @JoinColumn(name = "centre_id")
    private Centre centre;

    @Column(name = "visitor_cat_id")
    private Long visitorCatId;
}