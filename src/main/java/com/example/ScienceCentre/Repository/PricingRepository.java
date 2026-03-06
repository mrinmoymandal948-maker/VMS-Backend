package com.example.ScienceCentre.Repository;

import com.example.ScienceCentre.Model.PricingConfig;
import com.example.ScienceCentre.Model.Centre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PricingRepository extends JpaRepository<PricingConfig, Long>
{
        Optional<PricingConfig>
        findByCentreAndTicketTypeAndVisitorCategoryAndActive(
                Centre centre,
                String ticketType,
                String visitorCategory,
                Boolean active
        );
}