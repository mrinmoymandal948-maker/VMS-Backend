package com.example.ScienceCentre.Controller;

import com.example.ScienceCentre.DTO.RequestDto.RefundRequestDto;
import com.example.ScienceCentre.Enums.RefundStatus;
import com.example.ScienceCentre.Service.RefundService;
import com.example.ScienceCentre.Service.RefundServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    @Autowired
    private RefundService refundService;

    //  Fetch ticket details for refund screen
    @GetMapping("/ticket/{ticketNumber}")
    public ResponseEntity<?> getTicketForRefund(@PathVariable String ticketNumber) {
        return ResponseEntity.ok(refundService.getRefundableTicketDetails(ticketNumber));
    }

    @PostMapping("/request")
    public ResponseEntity<Void> requestRefund(@RequestBody RefundRequestDto dto) {
        refundService.processRefund(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/pending")
    public ResponseEntity<?> getPendingRefunds() {
        return ResponseEntity.ok(refundService.getPendingRefunds());
    }

    @PostMapping("/decision/{id}")
    public ResponseEntity<Void> decide(
            @PathVariable Long id,
            @RequestParam RefundStatus status,
            @RequestParam(required = false) String reason) {

        refundService.updateRefundStatus(id, status, reason);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{ticketNumber}")
    public ResponseEntity<?> getStatus(@PathVariable String ticketNumber) {
        return ResponseEntity.ok(refundService.getLatestRefund(ticketNumber));
    }
}
