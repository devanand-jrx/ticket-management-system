package com.devanand.tms.service;

import com.devanand.tms.contract.request.AgentRequest;
import com.devanand.tms.contract.response.AgentResponse;
import com.devanand.tms.exception.AgentNotFoundException;
import com.devanand.tms.model.Agent;
import com.devanand.tms.repository.AgentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AgentServiceTest {

    @InjectMocks
    private AgentService agentService;

    @Mock
    private AgentRepository agentRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateAgent() {
        AgentRequest agentRequest = new AgentRequest("Name", "name@example.org", "abcdd");
        Agent agent = Agent.builder()
                .name(agentRequest.getName())
                .email(agentRequest.getEmail())
                .password(agentRequest.getPassword())
                .build();
        AgentResponse expectedResponse = AgentResponse.builder()
                .id(1L)
                .name(agent.getName())
                .email(agent.getEmail())
                .build();

        when(agentRepository.save(any(Agent.class))).thenReturn(agent);
        when(modelMapper.map(agent, AgentResponse.class)).thenReturn(expectedResponse);

        AgentResponse actualResponse = agentService.createAgent(agentRequest);


        assertEquals(expectedResponse, actualResponse);
        verify(agentRepository, times(1)).save(any(Agent.class));
        verify(modelMapper, times(1)).map(agent, AgentResponse.class);
    }

    @Test
    void testGetAllAgents() {
        List<Agent> agents = new ArrayList<>();
        agents.add(new Agent());
        List<AgentResponse> agentResponses = new ArrayList<>();
        agentResponses.add(new AgentResponse());

        when(agentRepository.findAll()).thenReturn(agents);
        when(modelMapper.map(any(Agent.class), eq(AgentResponse.class))).thenReturn(agentResponses.get(0));

        List<AgentResponse> result = agentService.getAllAgents();

        assertEquals(agentResponses.size(), result.size());
        verify(agentRepository).findAll();
        verify(modelMapper, times(agents.size())).map(any(Agent.class), eq(AgentResponse.class));
    }

    @Test
    void testGetAgentById() {
        Long agentId = 1L;
        Agent agent = new Agent();
        AgentResponse agentResponse = new AgentResponse();

        when(agentRepository.findById(agentId)).thenReturn(Optional.of(agent));
        when(modelMapper.map(agent, AgentResponse.class)).thenReturn(agentResponse);

        AgentResponse result = agentService.getAgentById(agentId);

        assertEquals(agentResponse, result);
        verify(agentRepository).findById(agentId);
        verify(modelMapper).map(agent, AgentResponse.class);
    }

    @Test
    void testGetAgentById_ThrowsException() {
        Long agentId = 1L;

        when(agentRepository.findById(agentId)).thenReturn(Optional.empty());

        assertThrows(AgentNotFoundException.class, () -> agentService.getAgentById(agentId));
        verify(agentRepository).findById(agentId);
    }

    @Test
    void testUpdateAgent() {
        Long agentId = 1L;
        AgentRequest agentRequest = new AgentRequest("Updated Name", "updated.email@example.org", "updatedpassword");
        Agent agent = new Agent();
        Agent updatedAgent = new Agent();
        AgentResponse agentResponse = new AgentResponse();

        when(agentRepository.findById(agentId)).thenReturn(Optional.of(agent));
        when(agentRepository.save(agent)).thenReturn(updatedAgent);
        when(modelMapper.map(updatedAgent, AgentResponse.class)).thenReturn(agentResponse);

        AgentResponse result = agentService.updateAgent(agentId, agentRequest);

        assertEquals(agentResponse, result);
        verify(agentRepository).findById(agentId);
        verify(agentRepository).save(agent);
        verify(modelMapper).map(agentRequest, agent);
        verify(modelMapper).map(updatedAgent, AgentResponse.class);
    }

    @Test
    void testUpdateAgent_ThrowsException() {
        Long agentId = 1L;
        AgentRequest agentRequest = new AgentRequest("Updated Name", "updated.email@example.org", "updatedpassword");

        when(agentRepository.findById(agentId)).thenReturn(Optional.empty());

        assertThrows(AgentNotFoundException.class, () -> agentService.updateAgent(agentId, agentRequest));
        verify(agentRepository).findById(agentId);
    }

    @Test
    void testDeleteAgent() {
        Long agentId = 1L;

        when(agentRepository.existsById(agentId)).thenReturn(true);

        agentService.deleteAgent(agentId);

        verify(agentRepository).existsById(agentId);
        verify(agentRepository).deleteById(agentId);
    }

    @Test
    void testDeleteAgent_ThrowsException() {
        Long agentId = 1L;

        when(agentRepository.existsById(agentId)).thenReturn(false);

        assertThrows(AgentNotFoundException.class, () -> agentService.deleteAgent(agentId));
        verify(agentRepository).existsById(agentId);
    }
}
