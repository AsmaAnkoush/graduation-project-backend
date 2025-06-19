package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Appointment;
import com.bzu.smartvax.domain.Child;
import java.time.Instant;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Appointment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    @EntityGraph(
        attributePaths = {
            "child",
            "schedules",
            "schedules.vaccination",
            "schedules.vaccination.vaccineType",
            "healthWorker",
            "vaccinationCenter",
            "requestedNewCenter",
            "parent",
        }
    )
    List<Appointment> findByHealthWorkerIdAndAppointmentDateBetween(Long healthWorkerId, Instant start, Instant end);

    List<Appointment> findByStatusAndAppointmentDateBetween(String status, Instant start, Instant end);

    @EntityGraph(
        attributePaths = {
            "child",
            "schedules",
            "schedules.vaccination",
            "schedules.vaccination.vaccineType",
            "healthWorker",
            "vaccinationCenter",
            "requestedNewCenter",
            "parent",
        }
    )
    List<Appointment> findByChild_VaccinationCenter_Id(Long vaccinationCenterId);

    @EntityGraph(
        attributePaths = {
            "child",
            "schedules",
            "schedules.vaccination",
            "schedules.vaccination.vaccineType",
            "healthWorker",
            "vaccinationCenter",
            "requestedNewCenter",
            "parent",
        }
    )
    List<Appointment> findBySchedules_Id(Long scheduleVaccinationId);

    @EntityGraph(
        attributePaths = {
            "child",
            "schedules",
            "schedules.vaccination",
            "schedules.vaccination.vaccineType",
            "healthWorker",
            "vaccinationCenter",
            "requestedNewCenter",
            "parent",
        }
    )
    List<Appointment> findByParent_Id(Long parentId);

    @EntityGraph(
        attributePaths = {
            "child",
            "schedules",
            "schedules.vaccination",
            "schedules.vaccination.vaccineType",
            "healthWorker",
            "vaccinationCenter",
            "requestedNewCenter",
            "parent",
        }
    )
    List<Appointment> findByChild_VaccinationCenter_IdAndAppointmentDateBetween(Long centerId, Instant start, Instant end);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.child c JOIN FETCH a.vaccinationCenter vc WHERE vc.id = :centerId")
    List<Appointment> findAllByCenterWithDetails(@Param("centerId") Long centerId);

    @EntityGraph(
        attributePaths = {
            "child",
            "schedules",
            "schedules.vaccination",
            "schedules.vaccination.vaccineType",
            "healthWorker",
            "vaccinationCenter",
            "requestedNewCenter",
            "parent",
        }
    )
    List<Appointment> findByVaccinationCenterIdAndAppointmentDateBetween(Long centerId, Instant start, Instant end);

    List<Appointment> findByVaccinationCenter_Id(Long centerId);

    boolean existsByChildAndAppointmentDateAndStatus(Child child, Instant appointmentDate, String status);

    boolean existsByChildAndAppointmentDateAndStatusIn(Child child, Instant appointmentDate, List<String> statuses);

    @Query(
        """
            SELECT sv.vaccination.id, sv.child.id, MAX(a.id)
            FROM Appointment a
            JOIN a.schedules sv
            WHERE a.status = 'MISSED'
            GROUP BY sv.vaccination.id, sv.child.id
            HAVING COUNT(a) >= 2
        """
    )
    List<Object[]> findMissedTwiceSameVaccination();

    @Query(
        """
            SELECT a.child.id, MAX(a.id)
            FROM Appointment a
            WHERE a.status = 'MISSED'
            GROUP BY a.child.id
            HAVING COUNT(a) >= 3
        """
    )
    List<Object[]> findMissedThreeTimesForChild();
}
