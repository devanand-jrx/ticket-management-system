package com.devanand.tms.service;

import com.devanand.tms.constant.Status;
import com.devanand.tms.contract.request.TicketRequest;
import com.devanand.tms.contract.request.TicketUpdateRequest;
import com.devanand.tms.contract.response.TicketResponse;
import com.devanand.tms.exception.CustomerNotFoundException;
import com.devanand.tms.exception.TicketNotFoundException;
import com.devanand.tms.model.Customer;
import com.devanand.tms.model.Ticket;
import com.devanand.tms.repository.AgentRepository;
import com.devanand.tms.repository.TicketRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    private final AgentRepository agentRepository;

    private final ModelMapper modelMapper;

    public TicketResponse createTicket(Long customerId, TicketRequest ticketRequest) {
        log.info("Creating ticket for and customer ID: {}", ticketRequest.getCustomerId());
        Customer customer =
                agentRepository
                        .findById(customerId)
                        .orElseThrow(
                                () ->
                                        new CustomerNotFoundException(
                                                "Customer not found with id " + customerId));

        Ticket ticket =
                Ticket.builder()
                        .description(ticketRequest.getDescription())
                        .status(Status.OPEN)
                        .customer(customer)
                        .build();
        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Ticket created with ID: {}", savedTicket.getId());
        return modelMapper.map(savedTicket, TicketResponse.class);
    }

    public List<TicketResponse> getAllTickets() {
        log.info("Retrieving all tickets");
        return ticketRepository.findAll().stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponse.class))
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Long id) {
        log.info("Retrieving ticket with ID: {}", id);
        Ticket ticket =
                ticketRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new TicketNotFoundException(
                                                "Ticket not found with id " + id));
        log.info("Ticket found with ID: {}", id);
        return modelMapper.map(ticket, TicketResponse.class);
    }

    public TicketResponse updateTicket(Long id, TicketUpdateRequest ticketUpdateRequest) {
        log.info("Updating ticket with ID: {}", id);
        Ticket ticket =
                ticketRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new TicketNotFoundException(
                                                "Ticket not found with id " + id));
        modelMapper.map(ticketUpdateRequest, ticket);
        ticket.setStatus(Status.IN_PROGRESS);
        Ticket updatedTicket = ticketRepository.save(ticket);
        log.info("Ticket updated with ID: {}", updatedTicket.getId());
        return modelMapper.map(updatedTicket, TicketResponse.class);
    }

    public void deleteTicket(Long id) {
        log.info("Deleting ticket with ID: {}", id);
        if (!ticketRepository.existsById(id)) {
            log.error("Ticket not found with ID: {}", id);
            throw new TicketNotFoundException("Ticket not found with id " + id);
        }
        ticketRepository.deleteById(id);
        log.info("Ticket deleted with ID: {}", id);
    }
}
