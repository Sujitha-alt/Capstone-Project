package com.example.BusTicketReservation.service;

import com.example.BusTicketReservation.dto.BookingReportResponse;
import com.example.BusTicketReservation.dto.PaymentReportResponse;
import com.example.BusTicketReservation.entity.Booking;
import com.example.BusTicketReservation.entity.Payment;
import com.example.BusTicketReservation.repository.BookingRepository;
import com.example.BusTicketReservation.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class ReportService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    public List<BookingReportResponse> getBookingReport() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(b -> new BookingReportResponse(
                b.getId(),
                b.getTicketNumber(),
                b.getUser().getUsername(),
                b.getFromCity(),
                b.getToCity(),
                b.getSeats(),
                b.getTotalAmount(),
                b.getStatus(),
                b.getBus().getBusNumber(),
                b.getBus().getBusType(),
                b.getBookingDate())).collect(Collectors.toList());
    }

    public List<PaymentReportResponse> getPaymentReport() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream().map(p -> new PaymentReportResponse(
                p.getId(),
                p.getBooking().getId(),
                p.getBooking().getUser().getUsername(),
                p.getAmount(),
                p.getPaymentMethod(),
                p.getStatus(),
                p.getPaymentDate())).collect(Collectors.toList());
    }

    public ResponseEntity<byte[]> downloadBookingReportPdf() throws Exception {
        List<BookingReportResponse> reports = getBookingReport();

        Document doc = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);

        doc.open();
        doc.add(new Paragraph("Booking Report"));
        doc.add(new Paragraph("======================================"));

        for (BookingReportResponse r : reports) {
            doc.add(new Paragraph(
                    "Ticket: " + r.getTicketNumber() +
                            " | Passenger: " + r.getPassenger() +
                            " | Trip: " + r.getFromCity() + " -> " + r.getToCity() +
                            " | Seats: " + r.getSeats() +
                            " | Amount: " + r.getTotalAmount() +
                            " | Status: " + r.getStatus()));
        }

        doc.close();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=booking-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }

    public ResponseEntity<byte[]> downloadPaymentReportPdf() throws Exception {
        List<PaymentReportResponse> reports = getPaymentReport();

        Document doc = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(doc, out);

        doc.open();
        doc.add(new Paragraph("Payment Report"));
        doc.add(new Paragraph("======================================"));

        for (PaymentReportResponse r : reports) {
            doc.add(new Paragraph(
                    "PaymentId: " + r.getPaymentId() +
                            " | BookingId: " + r.getBookingId() +
                            " | Passenger: " + r.getPassenger() +
                            " | Amount: " + r.getAmount() +
                            " | Method: " + r.getPaymentMethod() +
                            " | Status: " + r.getStatus()));
        }

        doc.close();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payment-report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(out.toByteArray());
    }
}
