package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.Appointment;
import com.bzu.smartvax.domain.Child;
import java.time.Instant;
import java.time.LocalDate;
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
            "schedules.vaccination.group", // ✅ مضافة
            "schedules.vaccination.vaccineType",
            "healthWorker",
            "vaccinationCenter",
            "requestedNewCenter",
            "parent",
        }
    )
    List<Appointment> findByHealthWorkerIdAndAppointmentDateBetween(Long healthWorkerId, Instant start, Instant end);

    @EntityGraph(
        attributePaths = {
            "child",
            "schedules",
            "schedules.vaccination",
            "schedules.vaccination.group", // ✅ مضافة
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
            "schedules.vaccination.group", // ✅ مضافة
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
            "schedules.vaccination.group", // ✅ مضافة
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
            "schedules.vaccination.group", // ✅ موجودة مسبقًا
            "schedules.vaccination.vaccineType",
            "healthWorker",
            "vaccinationCenter",
            "requestedNewCenter",
            "parent",
        }
    )
    @Query("SELECT a FROM Appointment a WHERE a.parent.id = :parentId")
    List<Appointment> findByParentIdWithSchedules(@Param("parentId") Long parentId);

    @EntityGraph(
        attributePaths = {
            "child",
            "schedules",
            "schedules.vaccination",
            "schedules.vaccination.group", // ✅ مضافة
            "schedules.vaccination.vaccineType",
            "healthWorker",
            "vaccinationCenter",
            "requestedNewCenter",
            "parent",
        }
    )
    @Query(
        """
        SELECT COUNT(a) > 0 FROM Appointment a
        WHERE a.child = :child
          AND FUNCTION('DATE', a.appointmentDate) = :date
          AND a.status IN ('PENDING', 'reshdualing', 'confirmed', 'trlocation', 'completed')
        """
    )
    boolean existsValidAppointmentOnDate(@Param("child") Child child, @Param("date") LocalDate date);

    @EntityGraph(
        attributePaths = {
            "child",
            "schedules",
            "schedules.vaccination",
            "schedules.vaccination.group", // ✅ مضافة
            "schedules.vaccination.vaccineType",
            "healthWorker",
            "vaccinationCenter",
            "requestedNewCenter",
            "parent",
        }
    )
    List<Appointment> findByVaccinationCenterIdAndAppointmentDateBetween(Long centerId, Instant start, Instant end);

    // ✅ لو تستخدمه في صفحات فيها scheduleVaccination لازم تعدله، لكن يبدو إنه بسيط
    List<Appointment> findByChild_Id(String childId);

    @Query("SELECT a FROM Appointment a JOIN FETCH a.child c JOIN FETCH a.vaccinationCenter vc WHERE vc.id = :centerId")
    List<Appointment> findAllByCenterWithDetails(@Param("centerId") Long centerId);

    List<Appointment> findByVaccinationCenter_Id(Long centerId);

    boolean existsByChildAndAppointmentDateAndStatus(Child child, Instant appointmentDate, String status);

    boolean existsByChildAndAppointmentDateAndStatusIn(Child child, Instant appointmentDate, List<String> statuses);
}
