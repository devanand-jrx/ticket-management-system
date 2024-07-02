package com.devanand.tms.repository;

import com.devanand.tms.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Agent, Long> {}
