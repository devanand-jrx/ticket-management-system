package com.devanand.tms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devanand.tms.contract.request.TicketRequest;
import com.devanand.tms.contract.response.TicketResponse;
import com.devanand.tms.service.TicketService;
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
public class TicketControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private TicketService ticketService;

    @Autowired private ObjectMapper objectMapper;

    @Test
    public void testCreateTicket() throws Exception {
        TicketRequest ticketRequest = new TicketRequest();
        TicketResponse ticketResponse = new TicketResponse();

        when(ticketService.createTicket(any(TicketRequest.class))).thenReturn(ticketResponse);

        mockMvc.perform(
                        post("/ticket")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ticketRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponse)));
    }

    @Test
    public void testGetAllTickets() throws Exception {
        List<TicketResponse> ticketResponses = new ArrayList<>();

        when(ticketService.getAllTickets()).thenReturn(ticketResponses);

        mockMvc.perform(get("/ticket"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponses)));
    }

    @Test
    public void testGetTicketById() throws Exception {
        Long ticketId = 1L;
        TicketResponse ticketResponse = new TicketResponse();

        when(ticketService.getTicketById(ticketId)).thenReturn(ticketResponse);

        mockMvc.perform(get("/ticket/" + ticketId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponse)));
    }

    @Test
    public void testUpdateTicket() throws Exception {
        Long ticketId = 1L;
        TicketRequest ticketRequest = new TicketRequest();
        TicketResponse ticketResponse = new TicketResponse();

        when(ticketService.updateTicket(eq(ticketId), any(TicketRequest.class)))
                .thenReturn(ticketResponse);

        mockMvc.perform(
                        put("/ticket/" + ticketId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ticketRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponse)));
    }

    @Test
    public void testDeleteTicket() throws Exception {
        Long ticketId = 1L;

        doNothing().when(ticketService).deleteTicket(ticketId);

        mockMvc.perform(delete("/ticket/" + ticketId)).andExpect(status().isOk());
    }

    @Test
    public void testAssignTicketToAgent() throws Exception {
        Long ticketId = 1L;
        Long agentId = 1L;
        TicketResponse ticketResponse = new TicketResponse();

        when(ticketService.assignTicketToAgent(ticketId, agentId)).thenReturn(ticketResponse);

        mockMvc.perform(put("/ticket/" + ticketId + "/assign/" + agentId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponse)));
    }

    @Test
    public void testSearchByDescription() throws Exception {
        String description = "issue";
        List<TicketResponse> ticketResponses = new ArrayList<>();

        when(ticketService.searchByDescription(description)).thenReturn(ticketResponses);

        mockMvc.perform(post("/ticket/search/description").param("description", description))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponses)));
    }

    @Test
    public void testSearchByCustomer() throws Exception {
        String customer = "John Doe";
        List<TicketResponse> ticketResponses = new ArrayList<>();

        when(ticketService.searchByCustomer(customer)).thenReturn(ticketResponses);

        mockMvc.perform(post("/ticket/search/customer").param("customer", customer))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponses)));
    }
}
