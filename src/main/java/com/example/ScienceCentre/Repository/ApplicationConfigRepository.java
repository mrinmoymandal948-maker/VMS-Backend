package com.example.ScienceCentre.Repository;

import com.example.ScienceCentre.Model.ApplicationConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationConfigRepository extends JpaRepository<ApplicationConfig, Long>
{
    List<ApplicationConfig> findByCentreIdAndActiveTrue(Long centreId);
    Optional<ApplicationConfig> findByCentreIdAndConfigKeyAndActiveTrue(Long centreId, String configKey);
}
