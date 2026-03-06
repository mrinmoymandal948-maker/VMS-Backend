package com.example.ScienceCentre.Controller;

import com.example.ScienceCentre.Model.ApplicationConfig;
import com.example.ScienceCentre.Repository.ApplicationConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/application-config")
public class ApplicationConfigController {

    @Autowired
    private ApplicationConfigRepository applicationConfigRepository;

    @GetMapping("/{centreId}")
    public ResponseEntity<List<ApplicationConfig>> getConfigByCentre(
            @PathVariable Long centreId) {

        return ResponseEntity.ok(
                applicationConfigRepository
                        .findByCentreIdAndActiveTrue(centreId)
        );
    }
}
