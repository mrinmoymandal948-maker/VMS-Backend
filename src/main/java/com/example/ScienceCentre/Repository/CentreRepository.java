package com.example.ScienceCentre.Repository;

import com.example.ScienceCentre.Model.Centre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CentreRepository extends JpaRepository<Centre, Long>
{
    Optional<Centre> findByCentreTypeAndActiveTrue(String centreType);
}
