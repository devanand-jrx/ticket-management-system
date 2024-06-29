package com.devanand.tms.service;

import com.devanand.tms.constant.Status;
import com.devanand.tms.contract.request.TicketRequest;
import com.devanand.tms.contract.response.TicketResponse;
import com.devanand.tms.exception.AgentNotFoundException;
import com.devanand.tms.exception.CustomerNotFoundException;
import com.devanand.tms.exception.TicketNotFoundException;
import com.devanand.tms.model.Agent;
import com.devanand.tms.model.Customer;
import com.devanand.tms.model.Ticket;
import com.devanand.tms.repository.AgentRepository;
import com.devanand.tms.repository.CustomerRepository;
import com.devanand.tms.repository.TicketRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {
    @Autowired private final TicketRepository ticketRepository;

    @Autowired private final AgentRepository agentRepository;

    @Autowired private final CustomerRepository customerRepository;

    @Autowired private final ModelMapper modelMapper;

    public TicketResponse createTicket(TicketRequest ticketRequest) {
        Agent agent =
                agentRepository
                        .findById(ticketRequest.getAgentId())
                        .orElseThrow(
                                () ->
                                        new AgentNotFoundException(
                                                "Agent not found with id "
                                                        + ticketRequest.getAgentId()));
        Customer customer =
                customerRepository
                        .findById(ticketRequest.getCustomerId())
                        .orElseThrow(
                                () ->
                                        new CustomerNotFoundException(
                                                "Customer not found with id "
                                                        + ticketRequest.getCustomerId()));

        Ticket ticket =
                Ticket.builder()
                        .description(ticketRequest.getDescription())
                        .status(Status.valueOf(ticketRequest.getStatus()))
                        .customer(customer)
                        .build();

        return modelMapper.map(ticketRepository.save(ticket), TicketResponse.class);
    }

    public List<TicketResponse> getAllTickets() {
        return ticketRepository.findAll().stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponse.class))
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Long id) {
        Ticket ticket =
                ticketRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new TicketNotFoundException(
                                                "Ticket not found with id " + id));
        return modelMapper.map(ticket, TicketResponse.class);
    }

    public TicketResponse updateTicket(Long id, TicketRequest ticketRequest) {
        Ticket ticket =
                ticketRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new TicketNotFoundException(
                                                "Ticket not found with id " + id));
        modelMapper.map(ticketRequest, ticket);
        ticket.setStatus(Status.valueOf(ticketRequest.getStatus()));
        return modelMapper.map(ticketRepository.save(ticket), TicketResponse.class);
    }

    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new TicketNotFoundException("Ticket not found with id " + id);
        }
        ticketRepository.deleteById(id);
    }

    public TicketResponse assignTicketToAgent(Long ticketId, Long agentId) {
        Ticket ticket =
                ticketRepository
                        .findById(ticketId)
                        .orElseThrow(
                                () ->
                                        new TicketNotFoundException(
                                                "Ticket not found with id " + ticketId));
        Agent agent =
                agentRepository
                        .findById(agentId)
                        .orElseThrow(
                                () ->
                                        new TicketNotFoundException(
                                                "Agent not found with id " + agentId));
        ticket.setAgent(agent);
        Ticket savedTicket = ticketRepository.save(ticket);

        return TicketResponse.builder()
                .id(savedTicket.getId())
                .description(savedTicket.getDescription())
                .status(String.valueOf(savedTicket.getStatus()))
                .agentId(agentId)
                .customerId(savedTicket.getCustomer().getId())
                .build();
    }

    public TicketResponse updateTicketStatus(Long ticketId, String status) {
        Ticket ticket =
                ticketRepository
                        .findById(ticketId)
                        .orElseThrow(
                                () ->
                                        new TicketNotFoundException(
                                                "Ticket not found with id " + ticketId));
        ticket.setStatus(Status.valueOf(status));
        return modelMapper.map(ticketRepository.save(ticket), TicketResponse.class);
    }

    public List<TicketResponse> searchByDescription(String description) {
        return ticketRepository.findByDescriptionContaining(description).stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponse.class))
                .collect(Collectors.toList());
    }

    public List<TicketResponse> searchByCustomer(String customer) {
        return ticketRepository.findTicketsByCustomerLike(customer).stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponse.class))
                .collect(Collectors.toList());
    }

    public Ticket convertToEntity(TicketRequest ticketRequest) {
        Agent agent =
                agentRepository
                        .findById(ticketRequest.getAgentId())
                        .orElseThrow(() -> new AgentNotFoundException("Agent not found"));
        Customer customer =
                customerRepository
                        .findById(ticketRequest.getCustomerId())
                        .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return Ticket.builder()
                .description(ticketRequest.getDescription())
                .status(Status.valueOf(ticketRequest.getStatus()))
                .agent(agent)
                .customer(customer)
                .build();
    }

    public TicketResponse convertToResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .description(ticket.getDescription())
                .status(String.valueOf(ticket.getStatus()))
                .agentId(ticket.getAgent().getId())
                .customerId(ticket.getCustomer().getId())
                .build();
    }
}
