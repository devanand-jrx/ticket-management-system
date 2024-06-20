package com.devanand.tms.controller;

import com.devanand.tms.contract.request.AgentRequest;
import com.devanand.tms.contract.response.AgentResponse;
import com.devanand.tms.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/agents")
public class AdminController {

    @Autowired private AgentService agentService;

    @PostMapping
    public AgentResponse createAgent(@RequestBody AgentRequest agentRequest) {
        return agentService.createAgent(agentRequest);
    }
}
