package com.devanand.tms.service;

import com.devanand.tms.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgentService {

    @Autowired private AgentRepository agentRepository;
}
