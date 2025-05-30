package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Appointment;
import com.bzu.smartvax.domain.Child;
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
    @EntityGraph(
        attributePaths = {
            "child", "schedules", "schedules.vaccination", "healthWorker", "vaccinationCenter", "requestedNewCenter", "parent",
        }
    )
    List<Appointment> findByHealthWorkerIdAndAppointmentDateBetween(Long healthWorkerId, Instant start, Instant end);

    @EntityGraph(
        attributePaths = {
            "child", "schedules", "schedules.vaccination", "healthWorker", "vaccinationCenter", "requestedNewCenter", "parent",
        }
    )
    List<Appointment> findByChild_VaccinationCenter_Id(Long vaccinationCenterId);

    @EntityGraph(
        attributePaths = {
            "child", "schedules", "schedules.vaccination", "healthWorker", "vaccinationCenter", "requestedNewCenter", "parent",
        }
    )
    List<Appointment> findBySchedules_Id(Long scheduleVaccinationId);

    @EntityGraph(
        attributePaths = {
            "child", "schedules", "schedules.vaccination", "healthWorker", "vaccinationCenter", "requestedNewCenter", "parent",
        }
    )
    List<Appointment> findByParent_Id(Long parentId);

    boolean existsByChildAndAppointmentDateAndStatus(Child child, Instant appointmentDate, String status);

    boolean existsByChildAndAppointmentDateAndStatusIn(Child child, Instant appointmentDate, List<String> statuses);
}
