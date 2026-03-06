package com.example.ScienceCentre.Service;

import com.example.ScienceCentre.DTO.RequestDto.RefundRequestDto;
import com.example.ScienceCentre.DTO.ResponseDto.RefundResponseDto;
import com.example.ScienceCentre.Enums.RefundStatus;
import java.util.List;
import java.util.Map;

public interface RefundService {

    void processRefund(RefundRequestDto refundRequest);

    void updateRefundStatus(Long id, RefundStatus status, String reason);

    RefundResponseDto getLatestRefund(String ticketNumber);

    List<RefundResponseDto> getPendingRefunds();

    Map<String, Object> getRefundableTicketDetails(String ticketNumber);
}
