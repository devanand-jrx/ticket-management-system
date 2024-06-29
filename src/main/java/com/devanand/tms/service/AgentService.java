package com.devanand.tms.service;

import com.devanand.tms.contract.request.AgentRequest;
import com.devanand.tms.contract.response.AgentResponse;
import com.devanand.tms.exception.AgentNotFoundException;
import com.devanand.tms.model.Agent;
import com.devanand.tms.repository.AgentRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentService {
    @Autowired private final AgentRepository agentRepository;

    private final ModelMapper modelMapper;

    public AgentResponse createAgent(AgentRequest agentRequest) {
        Agent agent =
                Agent.builder()
                        .name(agentRequest.getName())
                        .email(agentRequest.getEmail())
                        .password(agentRequest.getPassword())
                        .build();
        agent = agentRepository.save(agent);
        return modelMapper.map(agent, AgentResponse.class);
    }

    public List<AgentResponse> getAllAgents() {
        return agentRepository.findAll().stream()
                .map(agent -> modelMapper.map(agent, AgentResponse.class))
                .collect(Collectors.toList());
    }

    public AgentResponse getAgentById(Long id) {
        Agent agent =
                agentRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new AgentNotFoundException("Agent not found with id " + id));
        return modelMapper.map(agent, AgentResponse.class);
    }

    public AgentResponse updateAgent(Long id, AgentRequest agentRequest) {
        Agent agent =
                agentRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new AgentNotFoundException("Agent not found with id " + id));
        modelMapper.map(agentRequest, agent);
        return modelMapper.map(agentRepository.save(agent), AgentResponse.class);
    }

    public void deleteAgent(Long id) {
        if (!agentRepository.existsById(id)) {
            throw new AgentNotFoundException("Agent not found with id " + id);
        }
        agentRepository.deleteById(id);
    }
}
