package com.devanand.tms.controller;

import com.devanand.tms.contract.request.CustomerRequest;
import com.devanand.tms.contract.request.TicketUpdateRequest;
import com.devanand.tms.contract.response.CustomerResponse;
import com.devanand.tms.contract.response.TicketResponse;
import com.devanand.tms.service.AgentService;
import java.util.List;
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
@RequestMapping("/agents")
public class AgentController {

    @Autowired private AgentService agentService;

    @PostMapping("/customers")
    public CustomerResponse createCustomer(@RequestBody CustomerRequest customerRequest) {
        return agentService.createCustomer(customerRequest);
    }

    @GetMapping("/customers")
    public List<CustomerResponse> getAllCustomers() {
        return agentService.getAllCustomers();
    }

    @GetMapping("/customers/{customerId}")
    public CustomerResponse getCustomerById(@PathVariable Long customerId) {
        return agentService.getCustomerById(customerId);
    }

    @PutMapping("/customers/{customerId}")
    public CustomerResponse updateCustomer(
            @PathVariable Long customerId, @RequestBody CustomerRequest customerRequest) {
        return agentService.updateCustomer(customerId, customerRequest);
    }

    @DeleteMapping("/customers/{customerId}")
    public void deleteCustomer(@PathVariable Long customerId) {
        agentService.deleteCustomer(customerId);
    }

    @GetMapping("/ticket")
    public List<TicketResponse> getAllTickets() {
        return agentService.getAllTickets();
    }

    @GetMapping("/ticket/{ticketId}")
    public TicketResponse getTicketById(@PathVariable Long ticketId) {
        return agentService.getTicketById(ticketId);
    }

    @PutMapping("/ticket/{ticketId}")
    public TicketResponse updateTicket(
            @PathVariable Long ticketId, @RequestBody TicketUpdateRequest ticketUpdateRequest) {
        return agentService.updateTicket(ticketId, ticketUpdateRequest);
    }

    @DeleteMapping("/ticket/{ticketId}")
    public void deleteTicket(@PathVariable Long ticketId) {
        agentService.deleteTicket(ticketId);
    }

    @PutMapping("/ticket/{ticketId}/assign/{agentId}")
    public TicketResponse assignTicketToAgent(
            @PathVariable Long ticketId, @PathVariable Long agentId) {
        return agentService.assignTicketToAgent(ticketId, agentId);
    }

    @PutMapping("/ticket/{ticketId}/status")
    public TicketResponse updateTicketStatus(
            @PathVariable Long ticketId, @RequestBody String status) {
        return agentService.updateTicketStatus(ticketId, status);
    }

    @PostMapping("/ticket/search/description")
    public List<TicketResponse> searchByDescription(@RequestParam String description) {
        return agentService.searchByDescription(description);
    }

    @PostMapping("/ticket/search/customer")
    public List<TicketResponse> searchByCustomer(@RequestParam String customer) {
        return agentService.searchByCustomer(customer);
    }
}
