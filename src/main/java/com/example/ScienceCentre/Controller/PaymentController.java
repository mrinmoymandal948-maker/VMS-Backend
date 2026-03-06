package com.example.ScienceCentre.Controller;

import com.example.ScienceCentre.DTO.RequestDto.PaymentRequestDto;
import com.example.ScienceCentre.DTO.ResponseDto.TicketResponseDto;
import com.example.ScienceCentre.Service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<TicketResponseDto> confirmPayment(@Valid @RequestBody PaymentRequestDto paymentRequest)
    {
        return ResponseEntity.ok(paymentService.confirmPayment(paymentRequest));
    }
}
