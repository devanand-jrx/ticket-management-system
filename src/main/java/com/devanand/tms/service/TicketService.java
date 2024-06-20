package com.devanand.tms.service;

import com.devanand.tms.repository.AgentRepository;
import com.devanand.tms.repository.CustomerRepository;
import com.devanand.tms.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    @Autowired private TicketRepository ticketRepository;

    @Autowired private AgentRepository agentRepository;

    @Autowired private CustomerRepository customerRepository;
}
