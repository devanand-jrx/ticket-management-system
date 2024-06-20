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
    @Autowired private AgentRepository agentRepository;

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
        return agentRepository
                .findById(id)
                .map(agent -> modelMapper.map(agent, AgentResponse.class))
                .orElseThrow(
                        () -> new AgentNotFoundException("Agent with id " + id + " not found"));
    }
}
