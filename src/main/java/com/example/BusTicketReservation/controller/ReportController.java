package com.example.BusTicketReservation.controller;

import com.example.BusTicketReservation.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/bookings/pdf")
    public ResponseEntity<byte[]> downloadBookingReport() throws Exception {
        return reportService.downloadBookingReportPdf();
    }

    @GetMapping("/payments/pdf")
    public ResponseEntity<byte[]> downloadPaymentReport() throws Exception {
        return reportService.downloadPaymentReportPdf();
    }
}
