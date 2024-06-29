package com.devanand.tms.repository;

import com.devanand.tms.model.Ticket;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByDescriptionContaining(String description);

    @Query("SELECT t FROM Ticket t WHERE t.customer.name LIKE :namePattern")
    List<Ticket> findTicketsByCustomerLike(@Param("namePattern") String namePattern);
}
