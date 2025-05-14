package com.bzu.smartvax.repository;

import com.bzu.smartvax.domain.ScheduleVaccination;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleVaccinationRepository extends JpaRepository<ScheduleVaccination, Long> {
    List<ScheduleVaccination> findByChildId(String childId);

    @Query("SELECT sv FROM ScheduleVaccination sv JOIN FETCH sv.vaccination WHERE sv.child.id IN :childIds")
    List<ScheduleVaccination> findAllWithVaccinationByChildIds(@Param("childIds") List<String> childIds);
}
