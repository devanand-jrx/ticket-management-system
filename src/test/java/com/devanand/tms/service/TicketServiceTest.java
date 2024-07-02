package com.devanand.tms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Mock private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTicket() {
        Long customerId = 1L;
        TicketRequest ticketRequest =
                new TicketRequest("description", null, customerId); // Adjusted constructor
        Customer customer = new Customer();
        Ticket ticket =
                Ticket.builder()
                        .description(ticketRequest.getDescription())
                        .status(Status.OPEN)
                        .customer(customer)
                        .build();
        TicketResponse expectedResponse = new TicketResponse();

        when(agentRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);
        when(modelMapper.map(ticket, TicketResponse.class)).thenReturn(expectedResponse);

        TicketResponse actualResponse = ticketService.createTicket(customerId, ticketRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(agentRepository).findById(customerId);
        verify(ticketRepository).save(any(Ticket.class));
        verify(modelMapper).map(ticket, TicketResponse.class);
    }

    @Test
    void testCreateTicket_CustomerNotFoundException() {
        Long customerId = 1L;
        TicketRequest ticketRequest = new TicketRequest("Description", 1L, 2L);
        when(agentRepository.findById(customerId)).thenReturn(Optional.empty());

        CustomerNotFoundException exception =
                assertThrows(
                        CustomerNotFoundException.class,
                        () -> ticketService.createTicket(customerId, ticketRequest));

        assertEquals("Customer not found with id " + customerId, exception.getMessage());
        verify(agentRepository).findById(customerId);
        verify(ticketRepository, never()).save(any(Ticket.class));
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
        TicketUpdateRequest ticketUpdateRequest = new TicketUpdateRequest("Updated Description");
        Ticket ticket = new Ticket();
        Ticket updatedTicket = new Ticket();
        TicketResponse ticketResponse = new TicketResponse();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(updatedTicket);
        when(modelMapper.map(updatedTicket, TicketResponse.class)).thenReturn(ticketResponse);

        TicketResponse result = ticketService.updateTicket(ticketId, ticketUpdateRequest);

        assertEquals(ticketResponse, result);
        verify(ticketRepository).findById(ticketId);
        verify(ticketRepository).save(ticket);
        verify(modelMapper).map(ticketUpdateRequest, ticket);
        verify(modelMapper).map(updatedTicket, TicketResponse.class);
    }

    @Test
    void testUpdateTicket_ThrowsException() {
        Long ticketId = 1L;
        TicketUpdateRequest ticketUpdateRequest = new TicketUpdateRequest("Updated Description");

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> ticketService.updateTicket(ticketId, ticketUpdateRequest));
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
}
