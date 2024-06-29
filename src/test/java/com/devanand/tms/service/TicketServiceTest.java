package com.devanand.tms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devanand.tms.constant.Status;
import com.devanand.tms.contract.request.TicketRequest;
import com.devanand.tms.contract.response.TicketResponse;
import com.devanand.tms.exception.TicketNotFoundException;
import com.devanand.tms.model.Agent;
import com.devanand.tms.model.Customer;
import com.devanand.tms.model.Ticket;
import com.devanand.tms.repository.AgentRepository;
import com.devanand.tms.repository.CustomerRepository;
import com.devanand.tms.repository.TicketRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class TicketServiceTest {

    @InjectMocks private TicketService ticketService;

    @Mock private TicketRepository ticketRepository;

    @Mock private AgentRepository agentRepository;

    @Mock private CustomerRepository customerRepository;

    @Mock private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTicket() {
        TicketRequest ticketRequest = new TicketRequest("Description", "OPEN", 1L, 1L);
        Agent agent = new Agent();
        Customer customer = new Customer();
        Ticket ticket =
                Ticket.builder()
                        .description(ticketRequest.getDescription())
                        .status(Status.valueOf(ticketRequest.getStatus()))
                        .customer(customer)
                        .build();
        TicketResponse expectedResponse =
                TicketResponse.builder()
                        .id(1L)
                        .description(ticket.getDescription())
                        .status(ticketRequest.getStatus())
                        .agentId(agent.getId())
                        .customerId(customer.getId())
                        .build();

        when(agentRepository.findById(ticketRequest.getAgentId())).thenReturn(Optional.of(agent));
        when(customerRepository.findById(ticketRequest.getCustomerId()))
                .thenReturn(Optional.of(customer));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(modelMapper.map(ticket, TicketResponse.class)).thenReturn(expectedResponse);

        TicketResponse actualResponse = ticketService.createTicket(ticketRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(agentRepository).findById(ticketRequest.getAgentId());
        verify(customerRepository).findById(ticketRequest.getCustomerId());
        verify(ticketRepository).save(any(Ticket.class));
        verify(modelMapper).map(ticket, TicketResponse.class);
    }

    @Test
    void testGetAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        List<TicketResponse> ticketResponses = new ArrayList<>();
        ticketResponses.add(new TicketResponse());

        when(ticketRepository.findAll()).thenReturn(tickets);
        when(modelMapper.map(any(Ticket.class), eq(TicketResponse.class)))
                .thenReturn(ticketResponses.get(0));

        List<TicketResponse> result = ticketService.getAllTickets();

        assertEquals(ticketResponses.size(), result.size());
        verify(ticketRepository).findAll();
        verify(modelMapper, times(tickets.size())).map(any(Ticket.class), eq(TicketResponse.class));
    }

    @Test
    void testGetTicketById() {
        Long ticketId = 1L;
        Ticket ticket = new Ticket();
        TicketResponse ticketResponse = new TicketResponse();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(modelMapper.map(ticket, TicketResponse.class)).thenReturn(ticketResponse);

        TicketResponse result = ticketService.getTicketById(ticketId);

        assertEquals(ticketResponse, result);
        verify(ticketRepository).findById(ticketId);
        verify(modelMapper).map(ticket, TicketResponse.class);
    }

    @Test
    void testGetTicketById_ThrowsException() {
        Long ticketId = 1L;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById(ticketId));
        verify(ticketRepository).findById(ticketId);
    }

    @Test
    void testUpdateTicket() {
        Long ticketId = 1L;
        TicketRequest ticketRequest = new TicketRequest("Updated Description", "CLOSED", 1L, 1L);
        Ticket ticket = new Ticket();
        Ticket updatedTicket = new Ticket();
        TicketResponse ticketResponse = new TicketResponse();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(updatedTicket);
        when(modelMapper.map(updatedTicket, TicketResponse.class)).thenReturn(ticketResponse);

        TicketResponse result = ticketService.updateTicket(ticketId, ticketRequest);

        assertEquals(ticketResponse, result);
        verify(ticketRepository).findById(ticketId);
        verify(ticketRepository).save(ticket);
        verify(modelMapper).map(ticketRequest, ticket);
        verify(modelMapper).map(updatedTicket, TicketResponse.class);
    }

    @Test
    void testUpdateTicket_ThrowsException() {
        Long ticketId = 1L;
        TicketRequest ticketRequest = new TicketRequest("Updated Description", "CLOSED", 1L, 1L);

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.updateTicket(ticketId, ticketRequest));
        verify(ticketRepository).findById(ticketId);
    }

    @Test
    void testDeleteTicket() {
        Long ticketId = 1L;

        when(ticketRepository.existsById(ticketId)).thenReturn(true);

        ticketService.deleteTicket(ticketId);

        verify(ticketRepository).existsById(ticketId);
        verify(ticketRepository).deleteById(ticketId);
    }

    @Test
    void testDeleteTicket_ThrowsException() {
        Long ticketId = 1L;

        when(ticketRepository.existsById(ticketId)).thenReturn(false);

        assertThrows(TicketNotFoundException.class, () -> ticketService.deleteTicket(ticketId));
        verify(ticketRepository).existsById(ticketId);
    }

    @Test
    void testAssignTicketToAgent() {
        Long ticketId = 1L;
        Long agentId = 1L;
        Agent agent = new Agent();
        Customer customer = new Customer();

        Ticket ticket = Ticket.builder().customer(customer).build();

        Ticket savedTicket =
                Ticket.builder()
                        .id(ticketId)
                        .description("Sample description")
                        .status(Status.OPEN)
                        .agent(agent)
                        .customer(customer)
                        .build();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(agentRepository.findById(agentId)).thenReturn(Optional.of(agent));
        when(ticketRepository.save(ticket)).thenReturn(savedTicket);

        TicketResponse expectedResponse =
                TicketResponse.builder()
                        .id(savedTicket.getId())
                        .description(savedTicket.getDescription())
                        .status(String.valueOf(savedTicket.getStatus()))
                        .agentId(agentId)
                        .customerId(savedTicket.getCustomer().getId())
                        .build();

        when(modelMapper.map(savedTicket, TicketResponse.class)).thenReturn(expectedResponse);

        TicketResponse result = ticketService.assignTicketToAgent(ticketId, agentId);

        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getDescription(), result.getDescription());
        assertEquals(expectedResponse.getStatus(), result.getStatus());
        assertEquals(expectedResponse.getAgentId(), result.getAgentId());
        assertEquals(expectedResponse.getCustomerId(), result.getCustomerId());
        verify(ticketRepository).findById(ticketId);
        verify(agentRepository).findById(agentId);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void testUpdateTicketStatus() {
        Long ticketId = 1L;
        String status = "CLOSED";
        Ticket ticket = new Ticket();
        TicketResponse ticketResponse = new TicketResponse();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(ticket);
        when(modelMapper.map(ticket, TicketResponse.class)).thenReturn(ticketResponse);

        TicketResponse result = ticketService.updateTicketStatus(ticketId, status);

        assertEquals(ticketResponse, result);
        verify(ticketRepository).findById(ticketId);
        verify(ticketRepository).save(ticket);
        verify(modelMapper).map(ticket, TicketResponse.class);
    }

    @Test
    void testSearchByDescription() {
        String description = "Description";
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        List<TicketResponse> ticketResponses = new ArrayList<>();
        ticketResponses.add(new TicketResponse());

        when(ticketRepository.findByDescriptionContaining(description)).thenReturn(tickets);
        when(modelMapper.map(any(Ticket.class), eq(TicketResponse.class)))
                .thenReturn(ticketResponses.get(0));

        List<TicketResponse> result = ticketService.searchByDescription(description);

        assertEquals(ticketResponses.size(), result.size());
        verify(ticketRepository).findByDescriptionContaining(description);
        verify(modelMapper, times(tickets.size())).map(any(Ticket.class), eq(TicketResponse.class));
    }

    @Test
    void testSearchByCustomer() {
        String customer = "Customer";
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        List<TicketResponse> ticketResponses = new ArrayList<>();
        ticketResponses.add(new TicketResponse());

        when(ticketRepository.findTicketsByCustomerLike(customer)).thenReturn(tickets);
        when(modelMapper.map(any(Ticket.class), eq(TicketResponse.class)))
                .thenReturn(ticketResponses.get(0));

        List<TicketResponse> result = ticketService.searchByCustomer(customer);

        assertEquals(ticketResponses.size(), result.size());
        verify(ticketRepository).findTicketsByCustomerLike(customer);
        verify(modelMapper, times(tickets.size())).map(any(Ticket.class), eq(TicketResponse.class));
    }
}
