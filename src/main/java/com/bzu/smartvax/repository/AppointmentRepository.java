package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Appointment;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Appointment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @EntityGraph(attributePaths = { "child", "schedule", "schedule.vaccination", "healthWorker" })
    List<Appointment> findByHealthWorkerIdAndAppointmentDateBetween(Long healthWorkerId, Instant start, Instant end);
}
