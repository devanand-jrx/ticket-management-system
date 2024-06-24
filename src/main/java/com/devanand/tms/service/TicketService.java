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
    @Autowired private TicketRepository ticketRepository;

    @Autowired private AgentRepository agentRepository;

    @Autowired private CustomerRepository customerRepository;

    @Autowired private ModelMapper modelMapper;

    public TicketResponse createTicket(Long agentId, Long CustomerId, TicketRequest ticketRequest) {
        Agent agent =
                agentRepository
                        .findById(agentId)
                        .orElseThrow(() -> new AgentNotFoundException("Agent not found"));
        Customer customer =
                customerRepository
                        .findById(CustomerId)
                        .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        Ticket ticket =
                modelMapper
                        .map(ticketRequest, Ticket.TicketBuilder.class)
                        .agent(agent)
                        .customer(customer)
                        .status(Status.valueOf(ticketRequest.getStatus()))
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
}
