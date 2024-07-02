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
import com.devanand.tms.contract.request.TicketUpdateRequest;
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
        Long customerId = 1L;
        TicketRequest ticketRequest = new TicketRequest("description", 2L, customerId);
        TicketResponse ticketResponse = new TicketResponse();

        when(ticketService.createTicket(eq(customerId), any(TicketRequest.class)))
                .thenReturn(ticketResponse);

        mockMvc.perform(
                        post("/ticket/{customerId}", customerId)
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

        mockMvc.perform(get("/ticket/{ticketId}", ticketId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponse)));
    }

    @Test
    public void testUpdateTicket() throws Exception {
        Long ticketId = 1L;
        TicketUpdateRequest ticketUpdateRequest = new TicketUpdateRequest("new description");
        TicketResponse ticketResponse = new TicketResponse();

        when(ticketService.updateTicket(eq(ticketId), any(TicketUpdateRequest.class)))
                .thenReturn(ticketResponse);

        mockMvc.perform(
                        put("/ticket/{ticketId}", ticketId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(ticketUpdateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponse)));
    }

    @Test
    public void testDeleteTicket() throws Exception {
        Long ticketId = 1L;

        doNothing().when(ticketService).deleteTicket(ticketId);

        mockMvc.perform(delete("/ticket/{ticketId}", ticketId)).andExpect(status().isOk());
    }
}
