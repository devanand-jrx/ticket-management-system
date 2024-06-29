package com.devanand.tms.controller;

import com.devanand.tms.contract.request.AgentRequest;
import com.devanand.tms.contract.response.AgentResponse;
import com.devanand.tms.service.AgentService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AgentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AgentService agentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAgent() throws Exception {
        AgentRequest agentRequest = new AgentRequest();
        AgentResponse agentResponse = new AgentResponse();

        when(agentService.createAgent(any(AgentRequest.class)))
                .thenReturn(agentResponse);

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

        when(agentService.getAllAgents())
                .thenReturn(agentResponses);

        mockMvc.perform(get("/admin/agents"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(agentResponses)));
    }

    @Test
    public void testGetAgentById() throws Exception {
        Long agentId = 1L;
        AgentResponse agentResponse = new AgentResponse();

        when(agentService.getAgentById(agentId))
                .thenReturn(agentResponse);

        mockMvc.perform(get("/admin/agents/" + agentId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(agentResponse)));
    }

    @Test
    public void testUpdateAgent() throws Exception {
        Long agentId = 1L;
        AgentRequest agentRequest = new AgentRequest();
        AgentResponse agentResponse = new AgentResponse();

        when(agentService.updateAgent(eq(agentId), any(AgentRequest.class)))
                .thenReturn(agentResponse);

        mockMvc.perform(
                        put("/admin/agents/" + agentId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(agentRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(agentResponse)));
    }

    @Test
    public void testDeleteAgent() throws Exception {
        Long agentId = 1L;

        doNothing().when(agentService).deleteAgent(agentId);

        mockMvc.perform(delete("/admin/agents/" + agentId))
                .andExpect(status().isOk());
    }
}