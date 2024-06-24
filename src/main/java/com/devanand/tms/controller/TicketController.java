package com.devanand.tms.controller;

import com.devanand.tms.contract.request.TicketRequest;
import com.devanand.tms.contract.response.TicketResponse;
import com.devanand.tms.service.TicketService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

    @Autowired private TicketService ticketService;

    @PostMapping
    public TicketResponse createTicket(@RequestBody TicketRequest ticketRequest) {
        return ticketService.createTicket(ticketRequest);
    }

    @GetMapping
    public List<TicketResponse> getAllTickets() {
        return ticketService.getAllTickets();
    }

    @GetMapping("/{id}")
    public TicketResponse getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById(id);
    }

    @PutMapping("/{id}")
    public TicketResponse updateTicket(
            @PathVariable Long id, @RequestBody TicketRequest ticketRequest) {
        return ticketService.updateTicket(id, ticketRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
    }

    @PutMapping("/{id}/assign/{agentId}")
    public TicketResponse assignTicketToAgent(@PathVariable Long id, @PathVariable Long agentId) {
        return ticketService.assignTicketToAgent(id, agentId);
    }

    @PutMapping("/{id}/status")
    public TicketResponse updateTicketStatus(@PathVariable Long id, @RequestBody String status) {
        return ticketService.updateTicketStatus(id, status);
    }

    @PostMapping("/search/description")
    public List<TicketResponse> searchByDescription(@RequestParam String description) {
        return ticketService.searchByDescription(description);
    }

    @PostMapping("/search/customer")
    public List<TicketResponse> searchByCustomer(@RequestParam String customer) {
        return ticketService.searchByCustomer(customer);
    }
}
