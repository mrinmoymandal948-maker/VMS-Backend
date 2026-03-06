package com.example.ScienceCentre.Controller;

import com.example.ScienceCentre.Model.LookupList;
import com.example.ScienceCentre.Repository.LookupListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lookups")
@CrossOrigin(origins = "http://localhost:3006", allowCredentials = "true")
public class LookupController {

    @Autowired
    private LookupListRepository lookupListRepository;

//     Fetches lookup values by lookup code
//     (e.g., VISITOR_CATEGORY, TICKET_TYPE)
    @GetMapping("/{headerCode}")
    public ResponseEntity<List<LookupList>> getLookups(@PathVariable String headerCode) {
        return ResponseEntity.ok(
                lookupListRepository.findByLookupCode(headerCode.toUpperCase())
        );
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<LookupList>> getByParent(@PathVariable Long parentId) {
        return ResponseEntity.ok(
                lookupListRepository.findByParentLookupDtlIdAndActiveFlag(parentId, "Y")
        );
    }
}
