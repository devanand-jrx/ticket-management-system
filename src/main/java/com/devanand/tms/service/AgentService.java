package com.devanand.tms.service;

import com.devanand.tms.contract.request.AgentRequest;
import com.devanand.tms.contract.response.AgentResponse;
import com.devanand.tms.exception.AgentNotFoundException;
import com.devanand.tms.model.Agent;
import com.devanand.tms.repository.AgentRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AgentService {
    @Autowired private final AgentRepository agentRepository;

    private final ModelMapper modelMapper;

    public AgentResponse createAgent(AgentRequest agentRequest) {
        log.info("Creating agent with email: {}", agentRequest.getEmail());
        Agent agent =
                Agent.builder()
                        .name(agentRequest.getName())
                        .email(agentRequest.getEmail())
                        .password(agentRequest.getPassword())
                        .build();
        agent = agentRepository.save(agent);
        log.info("Agent created with ID: {}", agent.getId());
        return modelMapper.map(agent, AgentResponse.class);
    }

    public List<AgentResponse> getAllAgents() {
        log.info("Retrieving all agents");
        return agentRepository.findAll().stream()
                .map(agent -> modelMapper.map(agent, AgentResponse.class))
                .collect(Collectors.toList());
    }

    public AgentResponse getAgentById(Long id) {
        log.info("Retrieving agent with ID: {}", id);
        Agent agent =
                agentRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new AgentNotFoundException("Agent not found with id " + id));
        log.info("Agent found with ID: {}", id);
        return modelMapper.map(agent, AgentResponse.class);
    }

    public AgentResponse updateAgent(Long id, AgentRequest agentRequest) {
        log.info("Updating agent with ID: {}", id);
        Agent agent =
                agentRepository
                        .findById(id)
                        .orElseThrow(
                                () -> new AgentNotFoundException("Agent not found with id " + id));
        modelMapper.map(agentRequest, agent);
        Agent updatedAgent = agentRepository.save(agent);
        log.info("Agent updated with ID: {}", updatedAgent.getId());
        return modelMapper.map(agentRepository.save(agent), AgentResponse.class);
    }

    public void deleteAgent(Long id) {
        log.info("Deleting agent with ID: {}", id);
        if (!agentRepository.existsById(id)) {
            log.error("Agent not found with ID: {}", id);
            throw new AgentNotFoundException("Agent not found with id " + id);
        }
        log.info("Agent deleted with ID: {}", id);
        agentRepository.deleteById(id);
    }
}
