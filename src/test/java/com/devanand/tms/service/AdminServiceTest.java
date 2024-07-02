package com.devanand.tms.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.devanand.tms.contract.request.AgentRequest;
import com.devanand.tms.contract.response.AgentResponse;
import com.devanand.tms.exception.AgentNotFoundException;
import com.devanand.tms.model.Agent;
import com.devanand.tms.repository.AdminRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

public class AdminServiceTest {

    @InjectMocks private AdminService adminService;

    @Mock private AdminRepository adminRepository;

    @Mock private ModelMapper modelMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAgent() {
        AgentRequest agentRequest = new AgentRequest("Name", "name@example.org", "password");
        Agent agent =
                Agent.builder()
                        .name(agentRequest.getName())
                        .email(agentRequest.getEmail())
                        .password(agentRequest.getPassword())
                        .build();
        AgentResponse expectedResponse = new AgentResponse(1L, "Name", "name@example.org");

        when(adminRepository.save(any(Agent.class))).thenReturn(agent);
        when(modelMapper.map(agent, AgentResponse.class)).thenReturn(expectedResponse);

        AgentResponse actualResponse = adminService.createAgent(agentRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(adminRepository, times(1)).save(any(Agent.class));
        verify(modelMapper, times(1)).map(agent, AgentResponse.class);
    }

    @Test
    void testGetAllAgents() {
        List<Agent> agents = new ArrayList<>();
        agents.add(new Agent());
        List<AgentResponse> agentResponses = new ArrayList<>();
        agentResponses.add(new AgentResponse());

        when(adminRepository.findAll()).thenReturn(agents);
        when(modelMapper.map(any(Agent.class), eq(AgentResponse.class)))
                .thenReturn(agentResponses.get(0));

        List<AgentResponse> result = adminService.getAllAgents();

        assertEquals(agentResponses.size(), result.size());
        verify(adminRepository).findAll();
        verify(modelMapper, times(agents.size())).map(any(Agent.class), eq(AgentResponse.class));
    }

    @Test
    void testGetAgentById() {
        Long agentId = 1L;
        Agent agent = new Agent();
        AgentResponse agentResponse = new AgentResponse();

        when(adminRepository.findById(agentId)).thenReturn(Optional.of(agent));
        when(modelMapper.map(agent, AgentResponse.class)).thenReturn(agentResponse);

        AgentResponse result = adminService.getAgentById(agentId);

        assertEquals(agentResponse, result);
        verify(adminRepository).findById(agentId);
        verify(modelMapper).map(agent, AgentResponse.class);
    }

    @Test
    void testGetAgentById_ThrowsException() {
        Long agentId = 1L;

        when(adminRepository.findById(agentId)).thenReturn(Optional.empty());

        assertThrows(AgentNotFoundException.class, () -> adminService.getAgentById(agentId));
        verify(adminRepository).findById(agentId);
    }

    @Test
    void testUpdateAgent() {
        Long agentId = 1L;
        AgentRequest agentRequest =
                new AgentRequest("Updated Name", "updated.email@example.org", "updatedpassword");
        Agent agent = new Agent();
        Agent updatedAgent = new Agent();
        AgentResponse agentResponse = new AgentResponse();

        when(adminRepository.findById(agentId)).thenReturn(Optional.of(agent));
        when(adminRepository.save(agent)).thenReturn(updatedAgent);
        when(modelMapper.map(updatedAgent, AgentResponse.class)).thenReturn(agentResponse);

        AgentResponse result = adminService.updateAgent(agentId, agentRequest);

        assertEquals(agentResponse, result);
        verify(adminRepository).findById(agentId);
        verify(adminRepository).save(agent);
        verify(modelMapper).map(agentRequest, agent);
        verify(modelMapper).map(updatedAgent, AgentResponse.class);
    }

    @Test
    void testUpdateAgent_ThrowsException() {
        Long agentId = 1L;
        AgentRequest agentRequest =
                new AgentRequest("Updated Name", "updated.email@example.org", "updatedpassword");

        when(adminRepository.findById(agentId)).thenReturn(Optional.empty());

        assertThrows(
                AgentNotFoundException.class,
                () -> adminService.updateAgent(agentId, agentRequest));
        verify(adminRepository).findById(agentId);
    }

    @Test
    void testDeleteAgent() {
        Long agentId = 1L;

        when(adminRepository.existsById(agentId)).thenReturn(true);

        adminService.deleteAgent(agentId);

        verify(adminRepository).existsById(agentId);
        verify(adminRepository).deleteById(agentId);
    }

    @Test
    void testDeleteAgent_ThrowsException() {
        Long agentId = 1L;

        when(adminRepository.existsById(agentId)).thenReturn(false);

        assertThrows(AgentNotFoundException.class, () -> adminService.deleteAgent(agentId));
        verify(adminRepository).existsById(agentId);
    }
}
