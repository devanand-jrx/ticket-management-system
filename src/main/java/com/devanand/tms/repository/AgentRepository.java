package com.devanand.tms.repository;

import com.devanand.tms.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentRepository extends JpaRepository<Customer, Long> {}
