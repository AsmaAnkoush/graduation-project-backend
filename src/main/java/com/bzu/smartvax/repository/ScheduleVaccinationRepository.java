package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.ScheduleVaccination;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleVaccinationRepository extends JpaRepository<ScheduleVaccination, Long> {
    List<ScheduleVaccination> findByChildId(String childId);

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

    // ✅ هذا هو التعديل المهم لتحميل مركز التطعيم مع الطفل
    @EntityGraph(attributePaths = { "child", "child.vaccinationCenter" })
    List<ScheduleVaccination> findAll();
}
