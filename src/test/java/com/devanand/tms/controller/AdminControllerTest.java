package com.devanand.tms.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.devanand.tms.contract.request.AgentRequest;
import com.devanand.tms.contract.response.AgentResponse;
import com.devanand.tms.service.AdminService;
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
public class AdminControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private AdminService adminService;

    @Autowired private ObjectMapper objectMapper;

    @Test
    public void testCreateAgent() throws Exception {
        AgentRequest agentRequest = new AgentRequest();
        AgentResponse agentResponse = new AgentResponse();

        when(adminService.createAgent(any(AgentRequest.class))).thenReturn(agentResponse);

        mockMvc.perform(
                        post("/admin/agents")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(agentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(agentResponse)));
    }

    @Test
    public void testGetAllAgents() throws Exception {
        List<AgentResponse> agentResponses = new ArrayList<>();

        when(adminService.getAllAgents()).thenReturn(agentResponses);

        mockMvc.perform(get("/admin/agents"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(agentResponses)));
    }

    @Test
    public void testGetAgentById() throws Exception {
        Long id = 1L;
        AgentResponse agentResponse = new AgentResponse();

        when(adminService.getAgentById(id)).thenReturn(agentResponse);

        mockMvc.perform(get("/admin/agents/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(agentResponse)));
    }

    @Test
    public void testUpdateAgent() throws Exception {
        Long id = 1L;
        AgentRequest agentRequest = new AgentRequest();
        AgentResponse agentResponse = new AgentResponse();

        when(adminService.updateAgent(eq(id), any(AgentRequest.class))).thenReturn(agentResponse);

        mockMvc.perform(
                        put("/admin/agents/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(agentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(agentResponse)));
    }

    @Test
    public void testDeleteAgent() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/admin/agents/{id}", id)).andExpect(status().isOk());

        verify(adminService).deleteAgent(eq(id));
    }
}
