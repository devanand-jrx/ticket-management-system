package com.devanand.tms.controller;

import com.devanand.tms.contract.request.AgentRequest;
import com.devanand.tms.contract.response.AgentResponse;
import com.devanand.tms.service.AgentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/agents")
public class AgentController {

    @Autowired private AgentService agentService;

    @PostMapping
    public AgentResponse createAgent(@RequestBody AgentRequest agentRequest) {
        return agentService.createAgent(agentRequest);
    }

    @GetMapping
    public List<AgentResponse> getAllAgents() {
        return agentService.getAllAgents();
    }

    @GetMapping("/{id}")
    public AgentResponse getAgentById(@PathVariable Long id) {
        return agentService.getAgentById(id);
    }

    @PutMapping("/{id}")
    public AgentResponse updateAgent(
            @PathVariable Long id, @RequestBody AgentRequest agentRequest) {
        return agentService.updateAgent(id, agentRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteAgent(@PathVariable Long id) {
        agentService.deleteAgent(id);
    }
}
