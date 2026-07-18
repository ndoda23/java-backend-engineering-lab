package org.example.ticketbookingsystem.repository;

import org.example.ticketbookingsystem.domain.Ticket;
import org.example.ticketbookingsystem.domain.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket,Long> {
     List<Ticket> findByEventIdAndStatus(Long eventId, TicketStatus status);

    /**
     * Conditional bulk update: only rows still in {@code expectedStatus} change.
     * Do not bump {@code version} here — JPA {@code @Version} owns that on entity saves;
     * the status predicate is the concurrency guard for this path.
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Ticket t SET t.status = :newStatus " +
            "WHERE t.id IN :ticketIds AND t.status = :expectedStatus")
    int updateTicketStatusSecurely(@Param("ticketIds") List<Long> ticketIds,
                                   @Param("newStatus") TicketStatus newStatus,
                                   @Param("expectedStatus") TicketStatus expectedStatus);

}

