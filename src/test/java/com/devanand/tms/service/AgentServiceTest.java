package com.devanand.tms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.devanand.tms.constant.Status;
import com.devanand.tms.contract.request.CustomerRequest;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class AgentServiceTest {

    @InjectMocks private AgentService agentService;

    @Mock private AgentRepository agentRepository;
    @Mock private TicketRepository ticketRepository;
    @Mock private AdminRepository adminRepository;

    @Mock private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCustomer() {
        CustomerRequest customerRequest = new CustomerRequest("Name", "name@example.com");
        Customer customer =
                Customer.builder()
                        .name(customerRequest.getName())
                        .email(customerRequest.getEmail())
                        .build();
        CustomerResponse expectedResponse =
                CustomerResponse.builder()
                        .id(1L)
                        .name(customer.getName())
                        .email(customer.getEmail())
                        .build();

        when(agentRepository.save(any(Customer.class))).thenReturn(customer);
        when(modelMapper.map(customer, CustomerResponse.class)).thenReturn(expectedResponse);

        CustomerResponse actualResponse = agentService.createCustomer(customerRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(agentRepository, times(1)).save(any(Customer.class));
        verify(modelMapper, times(1)).map(customer, CustomerResponse.class);
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer());
        List<CustomerResponse> customerResponses = new ArrayList<>();
        customerResponses.add(new CustomerResponse());

        when(agentRepository.findAll()).thenReturn(customers);
        when(modelMapper.map(any(Customer.class), eq(CustomerResponse.class)))
                .thenReturn(customerResponses.get(0));

        List<CustomerResponse> result = agentService.getAllCustomers();

        assertEquals(customerResponses.size(), result.size());
        verify(agentRepository).findAll();
        verify(modelMapper, times(customers.size()))
                .map(any(Customer.class), eq(CustomerResponse.class));
    }

    @Test
    void testGetCustomerById() {
        Long customerId = 1L;
        Customer customer = new Customer();
        CustomerResponse customerResponse = new CustomerResponse();

        when(agentRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerResponse.class)).thenReturn(customerResponse);

        CustomerResponse result = agentService.getCustomerById(customerId);

        assertEquals(customerResponse, result);
        verify(agentRepository).findById(customerId);
        verify(modelMapper).map(customer, CustomerResponse.class);
    }

    @Test
    void testGetCustomerById_ThrowsException() {
        Long customerId = 1L;

        when(agentRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class, () -> agentService.getCustomerById(customerId));
        verify(agentRepository).findById(customerId);
    }

    @Test
    void testUpdateCustomer() {
        Long customerId = 1L;
        CustomerRequest customerRequest =
                new CustomerRequest("Updated Name", "updated.email@example.com");
        Customer customer = new Customer();
        Customer updatedCustomer = new Customer();
        CustomerResponse customerResponse = new CustomerResponse();

        when(agentRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(agentRepository.save(customer)).thenReturn(updatedCustomer);
        when(modelMapper.map(updatedCustomer, CustomerResponse.class)).thenReturn(customerResponse);

        CustomerResponse result = agentService.updateCustomer(customerId, customerRequest);

        assertEquals(customerResponse, result);
        verify(agentRepository).findById(customerId);
        verify(agentRepository).save(customer);
        verify(modelMapper).map(customerRequest, customer);
        verify(modelMapper).map(updatedCustomer, CustomerResponse.class);
    }

    @Test
    void testUpdateCustomer_ThrowsException() {
        Long customerId = 1L;
        CustomerRequest customerRequest =
                new CustomerRequest("Updated Name", "updated.email@example.com");

        when(agentRepository.findById(customerId)).thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> agentService.updateCustomer(customerId, customerRequest));
        verify(agentRepository).findById(customerId);
    }

    @Test
    void testDeleteCustomer() {
        Long customerId = 1L;

        when(agentRepository.existsById(customerId)).thenReturn(true);

        agentService.deleteCustomer(customerId);

        verify(agentRepository).existsById(customerId);
        verify(agentRepository).deleteById(customerId);
    }

    @Test
    void testDeleteCustomer_ThrowsException() {
        Long customerId = 1L;

        when(agentRepository.existsById(customerId)).thenReturn(false);

        assertThrows(
                CustomerNotFoundException.class, () -> agentService.deleteCustomer(customerId));
        verify(agentRepository).existsById(customerId);
    }

    // Additional tests for ticket operations

    @Test
    void testGetAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        List<TicketResponse> ticketResponses = new ArrayList<>();
        ticketResponses.add(new TicketResponse());

        when(ticketRepository.findAll()).thenReturn(tickets);
        when(modelMapper.map(any(Ticket.class), eq(TicketResponse.class)))
                .thenReturn(ticketResponses.get(0));

        List<TicketResponse> result = agentService.getAllTickets();

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

        TicketResponse result = agentService.getTicketById(ticketId);

        assertEquals(ticketResponse, result);
        verify(ticketRepository).findById(ticketId);
        verify(modelMapper).map(ticket, TicketResponse.class);
    }

    @Test
    void testGetTicketById_ThrowsException() {
        Long ticketId = 1L;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> agentService.getTicketById(ticketId));
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

        TicketResponse result = agentService.updateTicket(ticketId, ticketUpdateRequest);

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
                () -> agentService.updateTicket(ticketId, ticketUpdateRequest));
        verify(ticketRepository).findById(ticketId);
    }

    @Test
    void testDeleteTicket() {
        Long ticketId = 1L;

        when(ticketRepository.existsById(ticketId)).thenReturn(true);

        agentService.deleteTicket(ticketId);

        verify(ticketRepository).existsById(ticketId);
        verify(ticketRepository).deleteById(ticketId);
    }

    @Test
    void testDeleteTicket_ThrowsException() {
        Long ticketId = 1L;

        when(ticketRepository.existsById(ticketId)).thenReturn(false);

        assertThrows(TicketNotFoundException.class, () -> agentService.deleteTicket(ticketId));
        verify(ticketRepository).existsById(ticketId);
    }

    @Test
    void testAssignTicketToAgent() {
        Long ticketId = 1L;
        Long agentId = 1L;
        Customer customer = Customer.builder().id(1L).build(); // Mocked or created customer
        Ticket ticket = Ticket.builder().id(ticketId).customer(customer).build();
        Agent agent = Agent.builder().id(agentId).build();
        Ticket updatedTicket =
                Ticket.builder().id(ticketId).customer(customer).agent(agent).build();
        TicketResponse ticketResponse =
                TicketResponse.builder().id(ticketId).agentId(agentId).build();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(adminRepository.findById(agentId)).thenReturn(Optional.of(agent));
        when(ticketRepository.save(ticket)).thenReturn(updatedTicket);
        when(modelMapper.map(updatedTicket, TicketResponse.class)).thenReturn(ticketResponse);

        TicketResponse result = agentService.assignTicketToAgent(ticketId, agentId);

        assertNotNull(result);
        assertEquals(ticketId, result.getId());
        assertEquals(agentId, result.getAgentId());

        verify(ticketRepository).findById(ticketId);
        verify(adminRepository).findById(agentId);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void testAssignTicketToAgent_TicketNotFound() {
        Long ticketId = 1L;
        Long agentId = 1L;

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> agentService.assignTicketToAgent(ticketId, agentId));
        verify(ticketRepository).findById(ticketId);
    }

    @Test
    void testAssignTicketToAgent_AgentNotFound() {
        Long ticketId = 1L;
        Long agentId = 1L;
        Ticket ticket = new Ticket();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(adminRepository.findById(agentId)).thenReturn(Optional.empty());

        assertThrows(
                AgentNotFoundException.class,
                () -> agentService.assignTicketToAgent(ticketId, agentId));
        verify(ticketRepository).findById(ticketId);
        verify(adminRepository).findById(agentId);
    }

    @Test
    void testUpdateTicketStatus() {
        Long ticketId = 1L;
        String status = "IN_PROGRESS";
        Ticket ticket = new Ticket();
        Ticket updatedTicket = new Ticket();
        updatedTicket.setStatus(Status.valueOf(status));
        TicketResponse ticketResponse = new TicketResponse();

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenReturn(updatedTicket);
        when(modelMapper.map(updatedTicket, TicketResponse.class)).thenReturn(ticketResponse);

        TicketResponse result = agentService.updateTicketStatus(ticketId, status);

        assertEquals(ticketResponse, result);
        verify(ticketRepository).findById(ticketId);
        verify(ticketRepository).save(ticket);
    }

    @Test
    void testUpdateTicketStatus_ThrowsException() {
        Long ticketId = 1L;
        String status = "IN_PROGRESS";

        when(ticketRepository.findById(ticketId)).thenReturn(Optional.empty());

        assertThrows(
                TicketNotFoundException.class,
                () -> agentService.updateTicketStatus(ticketId, status));
        verify(ticketRepository).findById(ticketId);
    }

    @Test
    void testSearchByDescription() {
        String description = "test";
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        List<TicketResponse> ticketResponses = new ArrayList<>();
        ticketResponses.add(new TicketResponse());

        when(ticketRepository.findByDescriptionContaining(description)).thenReturn(tickets);
        when(modelMapper.map(any(Ticket.class), eq(TicketResponse.class)))
                .thenReturn(ticketResponses.get(0));

        List<TicketResponse> result = agentService.searchByDescription(description);

        assertEquals(ticketResponses.size(), result.size());
        verify(ticketRepository).findByDescriptionContaining(description);
        verify(modelMapper, times(tickets.size())).map(any(Ticket.class), eq(TicketResponse.class));
    }

    @Test
    void testSearchByCustomer() {
        String customer = "test";
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        List<TicketResponse> ticketResponses = new ArrayList<>();
        ticketResponses.add(new TicketResponse());

        when(ticketRepository.findTicketsByCustomerLike(customer)).thenReturn(tickets);
        when(modelMapper.map(any(Ticket.class), eq(TicketResponse.class)))
                .thenReturn(ticketResponses.get(0));

        List<TicketResponse> result = agentService.searchByCustomer(customer);

        assertEquals(ticketResponses.size(), result.size());
        verify(ticketRepository).findTicketsByCustomerLike(customer);
        verify(modelMapper, times(tickets.size())).map(any(Ticket.class), eq(TicketResponse.class));
    }
}
