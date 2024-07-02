package com.devanand.tms.service;

import com.devanand.tms.constant.Status;
import com.devanand.tms.contract.request.CustomerRequest;
import com.devanand.tms.contract.request.TicketRequest;
import com.devanand.tms.contract.request.TicketUpdateRequest;
import com.devanand.tms.contract.response.CustomerResponse;
import com.devanand.tms.contract.response.TicketResponse;
import com.devanand.tms.exception.AgentNotFoundException;
import com.devanand.tms.exception.CustomerNotFoundException;
import com.devanand.tms.exception.TicketNotFoundException;
import com.devanand.tms.model.Agent;
import com.devanand.tms.model.Customer;
import com.devanand.tms.model.Ticket;
import com.devanand.tms.repository.AdminRepository;
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
public class AgentService {
    private final AgentRepository agentRepository;
    private final AdminRepository adminRepository;
    private final TicketRepository ticketRepository;

    private final ModelMapper modelMapper;

    public CustomerResponse createCustomer(CustomerRequest customerRequest) {

        log.info("Creating customer with email: {}", customerRequest.getEmail());
        Customer customer =
                Customer.builder()
                        .name(customerRequest.getName())
                        .email(customerRequest.getEmail())
                        .build();
        customer = agentRepository.save(customer);
        log.info("Customer created with ID: {}", customer.getId());
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public List<CustomerResponse> getAllCustomers() {
        log.info("Retrieving all customers");
        return agentRepository.findAll().stream()
                .map(customer -> modelMapper.map(customer, CustomerResponse.class))
                .collect(Collectors.toList());
    }

    public CustomerResponse getCustomerById(Long customerId) {
        log.info("Retrieving customer with ID: {}", customerId);
        Customer customer =
                agentRepository
                        .findById(customerId)
                        .orElseThrow(
                                () ->
                                        new CustomerNotFoundException(
                                                "Customer not found with id " + customerId));
        log.info("Customer found with ID: {}", customerId);
        return modelMapper.map(customer, CustomerResponse.class);
    }

    public CustomerResponse updateCustomer(Long customerId, CustomerRequest customerRequest) {
        log.info("Updating customer with ID: {}", customerId);
        Customer customer =
                agentRepository
                        .findById(customerId)
                        .orElseThrow(
                                () ->
                                        new CustomerNotFoundException(
                                                "Customer not found with id " + customerId));
        modelMapper.map(customerRequest, customer);
        Customer updatedCustomer = agentRepository.save(customer);
        log.info("Customer updated with ID: {}", updatedCustomer.getId());
        return modelMapper.map(updatedCustomer, CustomerResponse.class);
    }

    public void deleteCustomer(Long customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        if (!agentRepository.existsById(customerId)) {
            log.error("Customer not found with ID: {}", customerId);
            throw new CustomerNotFoundException("Customer not found with id " + customerId);
        }
        log.info("Customer deleted with ID: {}", customerId);
        agentRepository.deleteById(customerId);
    }

    public List<TicketResponse> getAllTickets() {
        log.info("Retrieving all tickets");
        return ticketRepository.findAll().stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponse.class))
                .collect(Collectors.toList());
    }

    public TicketResponse getTicketById(Long ticketId) {

        log.info("Retrieving ticket with ID: {}", ticketId);
        Ticket ticket =
                ticketRepository
                        .findById(ticketId)
                        .orElseThrow(
                                () ->
                                        new TicketNotFoundException(
                                                "Ticket not found with id " + ticketId));
        log.info("Ticket found with ID: {}", ticketId);
        return modelMapper.map(ticket, TicketResponse.class);
    }

    public TicketResponse updateTicket(Long ticketId, TicketUpdateRequest ticketUpdateRequest) {
        log.info("Updating ticket with ID: {}", ticketId);
        Ticket ticket =
                ticketRepository
                        .findById(ticketId)
                        .orElseThrow(
                                () ->
                                        new TicketNotFoundException(
                                                "Ticket not found with id " + ticketId));
        modelMapper.map(ticketUpdateRequest, ticket);
        ticket.setStatus(Status.IN_PROGRESS);
        Ticket updatedTicket = ticketRepository.save(ticket);
        log.info("Ticket updated with ID: {}", updatedTicket.getId());
        return modelMapper.map(updatedTicket, TicketResponse.class);
    }

    public void deleteTicket(Long ticketId) {
        log.info("Deleting ticket with ID: {}", ticketId);
        if (!ticketRepository.existsById(ticketId)) {
            log.error("Ticket not found with ID: {}", ticketId);
            throw new TicketNotFoundException("Ticket not found with id " + ticketId);
        }
        ticketRepository.deleteById(ticketId);
        log.info("Ticket deleted with ID: {}", ticketId);
    }

    public TicketResponse assignTicketToAgent(Long ticketId, Long agentId) {
        log.info("Assigning ticket with ID: {} to agent with ID: {}", ticketId, agentId);
        Ticket ticket =
                ticketRepository
                        .findById(ticketId)
                        .orElseThrow(
                                () ->
                                        new TicketNotFoundException(
                                                "Ticket not found with id " + ticketId));
        Agent agent =
                adminRepository
                        .findById(agentId)
                        .orElseThrow(
                                () ->
                                        new AgentNotFoundException(
                                                "Agent not found with id " + agentId));
        ticket.setAgent(agent);

        Ticket savedTicket = ticketRepository.save(ticket);
        log.info("Ticket with ID: {} assigned to agent with ID: {}", ticketId, agentId);

        return TicketResponse.builder()
                .id(savedTicket.getId())
                .description(savedTicket.getDescription())
                .status(String.valueOf(Status.IN_PROGRESS))
                .agentId(agentId)
                .customerId(savedTicket.getCustomer().getId())
                .build();
    }

    public TicketResponse updateTicketStatus(Long ticketId, String status) {
        log.info("Updating status of ticket with ID: {} to {}", ticketId, status);
        Ticket ticket =
                ticketRepository
                        .findById(ticketId)
                        .orElseThrow(
                                () ->
                                        new TicketNotFoundException(
                                                "Ticket not found with id " + ticketId));
        ticket.setStatus(Status.RESOLVED);
        log.info("Status of ticket with ID: {} updated to {}", ticketId, status);
        return modelMapper.map(ticketRepository.save(ticket), TicketResponse.class);
    }

    public List<TicketResponse> searchByDescription(String description) {
        log.info("Searching tickets with description containing: {}", description);
        return ticketRepository.findByDescriptionContaining(description).stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponse.class))
                .collect(Collectors.toList());
    }

    public List<TicketResponse> searchByCustomer(String customer) {
        log.info("Searching tickets by customer: {}", customer);
        return ticketRepository.findTicketsByCustomerLike(customer).stream()
                .map(ticket -> modelMapper.map(ticket, TicketResponse.class))
                .collect(Collectors.toList());
    }

    public Ticket convertToEntity(TicketRequest ticketRequest) {
        log.info("Converting TicketRequest to Ticket entity");
        Agent agent =
                adminRepository
                        .findById(ticketRequest.getAgentId())
                        .orElseThrow(() -> new AgentNotFoundException("Agent not found"));
        Customer customer =
                agentRepository
                        .findById(ticketRequest.getCustomerId())
                        .orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        return Ticket.builder()
                .description(ticketRequest.getDescription())
                .status(Status.RESOLVED)
                .agent(agent)
                .customer(customer)
                .build();
    }

    public TicketResponse convertToResponse(Ticket ticket) {
        log.info("Converting Ticket entity to TicketResponse");
        return TicketResponse.builder()
                .id(ticket.getId())
                .description(ticket.getDescription())
                .status(String.valueOf(ticket.getStatus()))
                .agentId(ticket.getAgent().getId())
                .customerId(ticket.getCustomer().getId())
                .build();
    }
}
