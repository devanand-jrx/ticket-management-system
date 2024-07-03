package com.devanand.tms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devanand.tms.contract.request.CustomerRequest;
import com.devanand.tms.contract.request.TicketUpdateRequest;
import com.devanand.tms.contract.response.CustomerResponse;
import com.devanand.tms.contract.response.TicketResponse;
import com.devanand.tms.service.AgentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AgentControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private AgentService agentService;

    @Autowired private ObjectMapper objectMapper;

    @Test
    public void testCreateCustomer() throws Exception {
        CustomerRequest customerRequest = new CustomerRequest();
        CustomerResponse customerResponse = new CustomerResponse();

        when(agentService.createCustomer(any(CustomerRequest.class))).thenReturn(customerResponse);

        mockMvc.perform(
                        post("/agents/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerResponse)));
    }

    @Test
    public void testGetAllCustomers() throws Exception {
        List<CustomerResponse> customerResponses = new ArrayList<>();

        when(agentService.getAllCustomers()).thenReturn(customerResponses);

        mockMvc.perform(get("/agents/customers"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerResponses)));
    }

    @Test
    public void testGetCustomerById() throws Exception {
        Long customerId = 1L;
        CustomerResponse customerResponse = new CustomerResponse();

        when(agentService.getCustomerById(customerId)).thenReturn(customerResponse);

        mockMvc.perform(get("/agents/customers/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerResponse)));
    }

    @Test
    public void testUpdateCustomer() throws Exception {
        Long customerId = 1L;
        CustomerRequest customerRequest = new CustomerRequest();
        CustomerResponse customerResponse = new CustomerResponse();

        when(agentService.updateCustomer(eq(customerId), any(CustomerRequest.class)))
                .thenReturn(customerResponse);

        mockMvc.perform(
                        put("/agents/customers/{customerId}", customerId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(customerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customerResponse)));
    }

    @Test
    public void testDeleteCustomer() throws Exception {
        Long customerId = 1L;

        mockMvc.perform(delete("/agents/customers/{customerId}", customerId))
                .andExpect(status().isOk());

        verify(agentService).deleteCustomer(eq(customerId));
    }

    @Test
    public void testGetAllTickets() throws Exception {
        List<TicketResponse> ticketResponses = new ArrayList<>();

        when(agentService.getAllTickets()).thenReturn(ticketResponses);

        mockMvc.perform(get("/agents/ticket"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponses)));
    }

    @Test
    public void testGetTicketById() throws Exception {
        Long ticketId = 1L;
        TicketResponse ticketResponse = new TicketResponse();

        when(agentService.getTicketById(ticketId)).thenReturn(ticketResponse);

        mockMvc.perform(get("/agents/ticket/{ticketId}", ticketId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponse)));
    }

    @Test
    public void testUpdateTicket() throws Exception {
        Long ticketId = 1L;
        TicketUpdateRequest ticketUpdateRequest = new TicketUpdateRequest();
        TicketResponse ticketResponse = new TicketResponse();

        when(agentService.updateTicket(eq(ticketId), any(TicketUpdateRequest.class)))
                .thenReturn(ticketResponse);

        mockMvc.perform(
                        put("/agents/ticket/{ticketId}", ticketId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ticketUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponse)));
    }

    @Test
    public void testDeleteTicket() throws Exception {
        Long ticketId = 1L;

        doNothing().when(agentService).deleteTicket(ticketId);

        mockMvc.perform(delete("/agents/ticket/{ticketId}", ticketId)).andExpect(status().isOk());
    }

    @Test
    public void testAssignTicketToAgent() throws Exception {
        Long ticketId = 1L;
        Long agentId = 1L;
        TicketResponse ticketResponse = new TicketResponse();

        when(agentService.assignTicketToAgent(ticketId, agentId)).thenReturn(ticketResponse);

        mockMvc.perform(put("/agents/ticket/{ticketId}/assign/{agentId}", ticketId, agentId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponse)));
    }

    @Test
    public void testUpdateTicketStatus() throws Exception {
        Long ticketId = 1L;
        String newStatus = "Status";
        TicketResponse ticketResponse =
                TicketResponse.builder()
                        .agentId(1L)
                        .customerId(1L)
                        .description("description")
                        .id(1L)
                        .status(newStatus)
                        .build();

        when(agentService.updateTicketStatus(eq(ticketId), any(String.class)))
                .thenReturn(ticketResponse);

        mockMvc.perform(
                        put("/agents/ticket/{ticketId}/status", ticketId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newStatus)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponse)));
    }

    @Test
    public void testSearchByDescription() throws Exception {
        String description = "issue";
        List<TicketResponse> ticketResponses = new ArrayList<>();

        when(agentService.searchByDescription(description)).thenReturn(ticketResponses);

        mockMvc.perform(post("/agents/ticket/search/description").param("description", description))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponses)));
    }

    @Test
    public void testSearchByCustomer() throws Exception {
        String customer = "John Doe";
        List<TicketResponse> ticketResponses = new ArrayList<>();

        when(agentService.searchByCustomer(customer)).thenReturn(ticketResponses);

        mockMvc.perform(post("/agents/ticket/search/customer").param("customer", customer))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponses)));
    }
}
