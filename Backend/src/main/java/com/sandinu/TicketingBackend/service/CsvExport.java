package com.sandinu.TicketingBackend.service;

import com.sandinu.TicketingBackend.model.Event;
import com.sandinu.TicketingBackend.model.TicketLog;
import lombok.AllArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.util.List;

public class CsvExport {
    private final EventService eventService;

    public CsvExport(EventService eventService) {
        this.eventService = eventService;
    }

    public ResponseEntity<byte[]> exportToCsv(String eventId) {
        Event event = eventService.getEventById(eventId);
        List<TicketLog> ticketLogList = event.getTicketLogs();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (CSVPrinter printer = new CSVPrinter(new OutputStreamWriter(outputStream), CSVFormat.DEFAULT.withHeader("Action", "Customer ID", "Vendor ID", "Timestamp", "Ticket Count"))) {
            for (TicketLog ticketLog : ticketLogList) {
                printer.printRecord(ticketLog.getAction(), ticketLog.getCustomerId(), ticketLog.getVendorId(), ticketLog.getTimestamp(), ticketLog.getTicketCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=ticket-logs.csv");

        return new ResponseEntity<>(outputStream.toByteArray(), headers, org.springframework.http.HttpStatus.OK);
    }

}
