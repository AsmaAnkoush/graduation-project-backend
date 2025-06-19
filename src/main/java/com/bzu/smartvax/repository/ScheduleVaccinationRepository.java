package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.ScheduleVaccination;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleVaccinationRepository extends JpaRepository<ScheduleVaccination, Long> {
    List<ScheduleVaccination> findByChildId(String childId);
    List<ScheduleVaccination> findByChild_IdAndScheduledDateAfter(String childId, LocalDate date);

    @Query(
        """
            SELECT s
            FROM ScheduleVaccination s
            JOIN FETCH s.vaccination v
            JOIN FETCH v.group
            WHERE s.child.id IN :childIds
        """
    )
    List<ScheduleVaccination> findAllWithVaccinationByChildIds(@Param("childIds") List<String> childIds);

    @Query(
        """
            SELECT sv FROM ScheduleVaccination sv
            LEFT JOIN FETCH sv.vaccination v
            LEFT JOIN FETCH v.group
            LEFT JOIN FETCH v.vaccineType
            LEFT JOIN FETCH sv.vaccinationGroup g
            LEFT JOIN FETCH sv.child c
            LEFT JOIN FETCH c.parent
            LEFT JOIN FETCH c.vaccinationCenter
            WHERE c.id = :id
        """
    )
    List<ScheduleVaccination> findAllWithVaccinationByChildId(@Param("id") String id);
}
