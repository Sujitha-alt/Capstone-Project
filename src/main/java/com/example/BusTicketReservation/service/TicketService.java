package com.example.BusTicketReservation.service;

import com.example.BusTicketReservation.dto.TicketDto;
import com.example.BusTicketReservation.entity.Booking;
import com.example.BusTicketReservation.entity.Bus;
import com.example.BusTicketReservation.entity.Payment;
import com.example.BusTicketReservation.repository.PaymentRepository;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.Color; // <-- only Color, not java.awt.*
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.google.zxing.client.j2se.MatrixToImageWriter.toBufferedImage;

@Service
public class TicketService {

    private final PaymentRepository paymentRepo;

    public TicketService(PaymentRepository paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    public TicketDto getByTicketId(Long id) {
        Payment p = paymentRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found"));
        return toDto(p);
    }

    public TicketDto getByPaymentId(Long paymentId) {
        return getByTicketId(paymentId);
    }

    public byte[] renderPdf(Long ticketId) {
        TicketDto t = getByTicketId(ticketId);

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document doc = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(doc, out);
            doc.open();

            Font h1 = new Font(Font.HELVETICA, 18, Font.BOLD, new Color(10, 161, 255));
            Font label = new Font(Font.HELVETICA, 11, Font.BOLD);
            Font text = new Font(Font.HELVETICA, 11);

            Paragraph title = new Paragraph("NeoBus E-Ticket", h1);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(12f);
            doc.add(title);

            // summary
            PdfPTable t1 = new PdfPTable(new float[] { 1.2f, 2.8f });
            t1.setWidthPercentage(100);
            addRow(t1, label, text, "Ticket #", t.getTicketNumber() + "  (id " + t.getId() + ")");
            addRow(t1, label, text, "Passenger", nullToDash(t.getPassenger()));
            addRow(t1, label, text, "Route", t.getFromCity() + " → " + t.getToCity());
            addRow(t1, label, text, "Seats", String.valueOf(t.getSeats()));
            addRow(t1, label, text, "Bus", nullToDash(t.getBusNumber()) + " · " + nullToDash(t.getBusType()));
            addRow(t1, label, text, "Amount", "₹" + t.getAmount());
            addRow(t1, label, text, "Status", t.getStatus());
            doc.add(t1);

            doc.add(new Paragraph(" "));
            String qrJson = "{\"ticketId\":" + t.getId()
                    + ",\"bookingId\":" + t.getBookingId()
                    + ",\"paymentId\":" + t.getPaymentId()
                    + ",\"ticketNumber\":\"" + safe(t.getTicketNumber()) + "\""
                    + ",\"route\":\"" + safe(t.getFromCity()) + "-" + safe(t.getToCity()) + "\""
                    + ",\"seats\":" + t.getSeats()
                    + ",\"amount\":" + t.getAmount() + "}";

            Image qrImg = Image.getInstance(qrPng(qrJson, 220, 220));
            qrImg.setAlignment(Element.ALIGN_RIGHT);
            doc.add(qrImg);

            Paragraph note = new Paragraph("Show this QR at boarding. Keep a valid ID.", text);
            note.setSpacingBefore(6f);
            doc.add(note);

            doc.close();
            return out.toByteArray();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "PDF generation failed: " + e.getMessage());
        }
    }

    private void addRow(PdfPTable table, Font label, Font text, String l, String v) {
        PdfPCell c1 = new PdfPCell(new Phrase(l, label));
        PdfPCell c2 = new PdfPCell(new Phrase(v == null ? "—" : v, text));
        c1.setBorder(Rectangle.NO_BORDER);
        c2.setBorder(Rectangle.NO_BORDER);
        c1.setPadding(6f);
        c2.setPadding(6f);
        table.addCell(c1);
        table.addCell(c2);
    }

    private String nullToDash(String s) {
        return (s == null || s.isBlank()) ? "—" : s;
    }

    private String safe(String s) {
        return s == null ? "" : s.replace("\"", "'");
    }

    private byte[] qrPng(String data, int w, int h) throws Exception {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, w, h, hints);
        BufferedImage img = toBufferedImage(matrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        return baos.toByteArray();
    }

    private TicketDto toDto(Payment p) {
        Booking b = p.getBooking();
        Bus bus = b != null ? b.getBus() : null;

        TicketDto t = new TicketDto();
        t.setId(p.getId()); // ticketId == paymentId
        t.setPaymentId(p.getId());
        t.setBookingId(b != null ? b.getId() : null);
        t.setTicketNumber(b != null ? b.getTicketNumber() : ("T-" + p.getId()));
        t.setPassenger(b != null && b.getUser() != null ? b.getUser().getUsername() : null);
        t.setFromCity(b != null ? b.getFromCity() : null);
        t.setToCity(b != null ? b.getToCity() : null);
        t.setSeats(b != null ? b.getSeats() : 0);

        // no null check against primitive double
        double amount = p.getAmount(); // if entity uses primitive double
        if (amount == 0 && b != null)
            amount = b.getTotalAmount();
        t.setAmount(amount);

        t.setStatus(p.getStatus());
        t.setBookingDate(b != null ? b.getBookingDate() : null);
        t.setBusNumber(bus != null ? bus.getBusNumber() : null);
        t.setBusType(bus != null ? bus.getBusType() : null);
        return t;
    }
}
