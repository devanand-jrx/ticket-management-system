package com.devanand.tms.controller;

import com.devanand.tms.contract.request.TicketRequest;
import com.devanand.tms.contract.request.TicketUpdateRequest;
import com.devanand.tms.contract.response.TicketResponse;
import com.devanand.tms.service.TicketService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/{customerId}")
    public TicketResponse createTicket(
            @PathVariable Long customerId, @RequestBody TicketRequest ticketRequest) {
        return ticketService.createTicket(customerId, ticketRequest);
    }

    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{ticketId}")
    public TicketResponse getTicketById(@PathVariable Long ticketId) {
        return ticketService.getTicketById(ticketId);
    }

    @PutMapping("/{ticketId}")
    public TicketResponse updateTicket(
            @PathVariable Long ticketId, @RequestBody TicketUpdateRequest ticketUpdateRequest) {
        return ticketService.updateTicket(ticketId, ticketUpdateRequest);
    }

    @DeleteMapping("/{ticketId}")
    public void deleteTicket(@PathVariable Long ticketId) {
        ticketService.deleteTicket(ticketId);
    }
}
